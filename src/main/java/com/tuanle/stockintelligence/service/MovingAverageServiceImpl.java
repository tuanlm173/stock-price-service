package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.dto.StockPriceAverage;
import com.tuanle.stockintelligence.dto.TwoHundredMovingAvg;
import com.tuanle.stockintelligence.repository.ClosePriceRepository;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Service
public class MovingAverageServiceImpl implements MovingAverageService {

    Logger logger = LoggerFactory.getLogger(MovingAverageServiceImpl.class);

    private ClosePriceRepository closePriceRepository;
    private SuggestedDateService suggestedDateService;

    @Autowired
    public MovingAverageServiceImpl(ClosePriceRepository closePriceRepository, SuggestedDateService suggestedDateService) {
        this.closePriceRepository = closePriceRepository;
        this.suggestedDateService = suggestedDateService;
    }


    @Override
    public double getAverage(JSONArray jsonDataArray) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(jsonDataArray.iterator(), Spliterator.ORDERED), false)
                .map(JSONArray.class::cast)
                .mapToDouble(closePrice -> closePrice.getDouble(1))
                .average()
                .getAsDouble();
    }

    @Override
    public TwoHundredMovingAvg getMovingAverageForStock(String ticker, String startDate) {
        closePriceRepository.get200Price(ticker, startDate);
        JSONArray dataArray = closePriceRepository.getListOf200PriceJSONArray();
        try {
            suggestedDateService.suggestStartDate(ticker, startDate, dataArray);
        } catch (IOException e) {
            logger.error("Error when fetching close price from source", e);
        }
        double average = getAverage(dataArray);
        StockPriceAverage stockPriceAvg = StockPriceAverage.of(ticker, String.valueOf(average));
        return TwoHundredMovingAvg.of(stockPriceAvg);
    }

}
