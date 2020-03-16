package com.tuanle.stockintelligence.dto;

public class ClosePrice {

    private Price price;

    private ClosePrice(Price price) {
        this.price = price;
    }

    public static ClosePrice of(Price price) {
        return new ClosePrice(price);
    }

    public Price getPrice() {
        return price;
    }
}
