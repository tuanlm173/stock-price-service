package com.tuanle.stockintelligence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TwoHundredMovingAvg {

    @JsonProperty(value = "200dma")
    private StockPriceAverage stockPriceAverage;


    private TwoHundredMovingAvg(StockPriceAverage stockPriceAverage) {
        this.stockPriceAverage = stockPriceAverage;
    }

    public static TwoHundredMovingAvg of(StockPriceAverage stockPriceAverage) {
        return new TwoHundredMovingAvg(stockPriceAverage);
    }

    public StockPriceAverage getStockPriceAverage() {
        return stockPriceAverage;
    }
}
