package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.dto.ClosePrice;

import java.util.List;

public interface ClosePriceService {

    List<List<String>> getListClosePrice(String ticker, String startDate, String endDate);

    ClosePrice getTickerAndClosePrice(String ticker, String startDate, String endDate);
}
