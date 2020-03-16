package com.tuanle.stockintelligence.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MultiTwoHundredMovingAvg {

    @JsonProperty(value = "200dma")
    public List<StockPriceAverage> getMultiStockPriceAverage() {
        return multiStockPriceAverage;
    }

    private List<StockPriceAverage> multiStockPriceAverage;


    private MultiTwoHundredMovingAvg(List<StockPriceAverage> multiStockPriceAverage) {
        this.multiStockPriceAverage = multiStockPriceAverage;
    }

    public static MultiTwoHundredMovingAvg of(List<StockPriceAverage> multiStockPriceAverage) {
        return new MultiTwoHundredMovingAvg(multiStockPriceAverage);
    }
}
