package com.tuanle.stockintelligence.dto;

public class StockPriceAverage {

    private String ticker;
    private String avg;


    private StockPriceAverage(String ticker, String avg) {
        this.ticker = ticker;
        this.avg = avg;
    }

    public static StockPriceAverage of(String ticker, String avg) {
        return new StockPriceAverage(ticker, avg);
    }

    public String getTicker() {
        return ticker;
    }

    public String getAvg() {
        return avg;
    }
}
