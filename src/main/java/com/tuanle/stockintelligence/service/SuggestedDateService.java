package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.exception.InvalidParameterException;
import com.tuanle.stockintelligence.exception.ResourceNotExistException;
import org.json.JSONArray;

import java.io.IOException;

public interface SuggestedDateService {

    void getSuggestion(String ticker, String startDate, JSONArray dataArray) throws ResourceNotExistException, IOException;

    String[] getSuggestedStartDate(String ticker, String startDateParam) throws InvalidParameterException, IOException;

    void getDefaultFirstDateData(String ticker, String firstDate) throws IOException;

    JSONArray getDefaultFirstDateDataJSONArray();

    default void suggestDefaultFirstDate(String ticker, String firstDate) throws IOException {
        getDefaultFirstDateData(ticker, firstDate);
    }

    default void suggestStartDate(String ticker, String startDate, JSONArray dataArray) throws ResourceNotExistException, InvalidParameterException, IOException {
        getSuggestion(ticker, startDate, dataArray);
    }
}
