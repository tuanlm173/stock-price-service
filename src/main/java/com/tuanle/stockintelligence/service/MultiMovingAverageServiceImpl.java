package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.dto.MultiTwoHundredMovingAvg;
import com.tuanle.stockintelligence.dto.StockPriceAverage;
import com.tuanle.stockintelligence.handler.ErrorHandling;
import com.tuanle.stockintelligence.repository.ClosePriceRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.CompletableFuture.allOf;

@Service
public class MultiMovingAverageServiceImpl implements MultiMovingAverageService {

    Logger logger = LoggerFactory.getLogger(MultiMovingAverageServiceImpl.class);

    private final OkHttpClient client;
    private final ClosePriceRepository closePriceRepository;
    private final SuggestedDateService suggestedDateService;
    private final MovingAverageService movingAverageService;

    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public MultiMovingAverageServiceImpl(OkHttpClient client, ClosePriceRepository closePriceRepository, SuggestedDateService suggestedDateService, MovingAverageService movingAverageService) {
        this.client = client;
        this.closePriceRepository = closePriceRepository;
        this.suggestedDateService = suggestedDateService;
        this.movingAverageService = movingAverageService;
    }

    @Override
    public MultiTwoHundredMovingAvg getMultiMovingAverageForStocks(String tickersParam, String startDateParam) {
        String[] tickers = tickersParam.split(",");

        List<CompletableFuture<StockPriceAverage>> futureList = new ArrayList<>();
        long startTime = System.nanoTime();
        for (String ticker : tickers) {
            String requestUrl = String.format(closePriceRepository.get200DayMovingAverageUrl(), ticker, startDateParam);
            Request request = new Request.Builder().url(requestUrl).build();
            CompletableFuture<StockPriceAverage> future = CompletableFuture.supplyAsync(() -> {
              try {
                  logger.info("Start fetching close price for ticker {} from {}", ticker, startDateParam);
                  Response response = client.newCall(request).execute();
                  String responseStr;
                  if (response.body() != null) {
                      responseStr = response.body().string();
                      JSONObject responseObj = new JSONObject(responseStr);
                      if (!ErrorHandling.isTickerValid(responseObj)) {
                          return StockPriceAverage.of(ticker, ErrorHandling.INVALID_TICKER_SYMBOL_ERROR_MESSAGE);
                      }
                      String[] suggestedDate = suggestedDateService.getSuggestedStartDate(ticker, startDateParam);
                      df.setLenient(false);
                      Date endDateParsed = df.parse(suggestedDate[1]);
                      Date dateParsed = df.parse(startDateParam);
                      if (!dateParsed.before(endDateParsed)) {
                          String firstPossibleDate = suggestedDate[0];
                          suggestedDateService.suggestDefaultFirstDate(ticker, firstPossibleDate);
                          JSONArray defaultFirstDateData = suggestedDateService.getDefaultFirstDateDataJSONArray();
                          double defaultAverage = movingAverageService.getAverage(defaultFirstDateData);
                          return StockPriceAverage.of(ticker, String.valueOf(defaultAverage));
                      }
                      JSONObject datasetObject = (JSONObject) responseObj.get("dataset");
                      JSONArray dataArray = (JSONArray) datasetObject.get("data");
                      double average = movingAverageService.getAverage(dataArray);
                      return StockPriceAverage.of(ticker, String.valueOf(average));
                  }

              } catch (IOException | ParseException e) {
                  logger.error("Error when fetching data from source", e);
              }
              return null;
            });
            futureList.add(future);
        }

        allOf(futureList.toArray(new CompletableFuture[]{})).join();

        List<StockPriceAverage> stockAverages = new ArrayList<>();
        for (CompletableFuture<StockPriceAverage> future : futureList) {
            StockPriceAverage stockAverage = null;
            try {
                stockAverage = future.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error when fetching data from source", e);
            }
            if (stockAverage != null) {
                stockAverages.add(stockAverage);
            }
        }

        return MultiTwoHundredMovingAvg.of(stockAverages);
    }
}
