package com.tuanle.stockintelligence.repository;

import com.tuanle.stockintelligence.exception.InvalidParameterException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public interface ClosePriceRepository {

    Logger logger = LoggerFactory.getLogger(ClosePriceRepository.class);

    String getClosePriceUrl();

    String get200DayMovingAverageUrl();

    void getListOfClosePriceFromSource(String ticker, String startDate, String endDate) throws InvalidParameterException, IOException;

    void getListOf200PriceFromSource(String ticker, String startDate) throws InvalidParameterException, IOException;

    JSONArray getClosePriceJSONArray();

    JSONArray getListOf200PriceJSONArray();

    default void getClosePrice(String ticker, String startDate, String endDate) {
        long startTime = System.nanoTime();
        try {
            logger.info("Start fetching close price for ticker {} from {} to {}", ticker, startDate, endDate);
            getListOfClosePriceFromSource(ticker, startDate, endDate);
            long duration = (System.nanoTime() - startTime)/1000000;
            logger.info("Successfully got 1 item from source, took {} milliseconds", duration);
        } catch (IOException e) {
            logger.error("Error when fetching close price from source", e);
        }
    }

    default void get200Price(String ticker, String startDate) {
        long startTime = System.nanoTime();
        try {
            logger.info("Start fetching 200 day of price for ticker {} from day {}", ticker, startDate);
            getListOf200PriceFromSource(ticker, startDate);
            long duration = (System.nanoTime() - startTime)/1000000;
            logger.info("Successfully got 1 item from source, took {} milliseconds", duration);
        } catch (IOException e) {
            logger.error("Error when fetching close price from source", e);
        }
    }
}
