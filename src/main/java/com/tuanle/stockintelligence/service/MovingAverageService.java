package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.dto.TwoHundredMovingAvg;
import org.json.JSONArray;

public interface MovingAverageService {

    double getAverage(JSONArray jsonDataArray);

    TwoHundredMovingAvg getMovingAverageForStock(String ticker, String startDate);

}
