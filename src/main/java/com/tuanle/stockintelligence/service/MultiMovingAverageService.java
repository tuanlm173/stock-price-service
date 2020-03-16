package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.dto.MultiTwoHundredMovingAvg;

import java.util.concurrent.ExecutionException;

public interface MultiMovingAverageService {

    MultiTwoHundredMovingAvg getMultiMovingAverageForStocks(String tickersParam, String startDateParam);
}
