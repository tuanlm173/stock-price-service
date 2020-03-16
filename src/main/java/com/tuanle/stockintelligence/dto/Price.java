package com.tuanle.stockintelligence.dto;


import java.util.List;


public class Price {

    private String ticker;
    private List<List<String>> dateClose;

    private Price(String ticker, List<List<String>> dateClose) {
        this.ticker = ticker;
        this.dateClose = dateClose;
    }

    public static Price of(String ticker, List<List<String>> dateClose) {
        return new Price(ticker, dateClose);
    }

    public String getTicker() {
        return ticker;
    }

    public List<List<String>> getDateClose() {
        return dateClose;
    }
}
