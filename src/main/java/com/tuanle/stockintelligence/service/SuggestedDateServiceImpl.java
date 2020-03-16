package com.tuanle.stockintelligence.service;

import com.tuanle.stockintelligence.config.GeneralConfig;
import com.tuanle.stockintelligence.exception.InvalidParameterException;
import com.tuanle.stockintelligence.exception.ResourceNotExistException;
import com.tuanle.stockintelligence.handler.ErrorHandling;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SuggestedDateServiceImpl implements SuggestedDateService {

    private static final String GET_FIRST_POSSIBLE_START_DATE =
            GeneralConfig.API_URL + "%s.json?start_date=%s&column_index=4&collapse=daily&order=asc&limit=1&api_key=" + GeneralConfig.API_KEY;

    private static final String GET_200_DAY_MOVING_AVG =
            GeneralConfig.API_URL + "%s.json?start_date=%s&column_index=4&collapse=daily&order=asc&limit=200&api_key=" + GeneralConfig.API_KEY;

    private final OkHttpClient client;

    private JSONArray defaultFirstDateDataJSONArray = null;

    public SuggestedDateServiceImpl(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void getSuggestion(String ticker, String startDate, JSONArray dataArray) throws ResourceNotExistException, IOException {
        if(dataArray.toList().isEmpty()) {
            String[] suggestedStartDate = getSuggestedStartDate(ticker, startDate);
            throw new ResourceNotExistException(String.format(ErrorHandling.SUGGESTED_AVAIL_START_DATE_MESSAGE, suggestedStartDate[0], suggestedStartDate[1]));
        }
    }

    @Override
    public String[] getSuggestedStartDate(String ticker, String startDateParam) throws InvalidParameterException, IOException {
        String requestUrl = String.format(GET_FIRST_POSSIBLE_START_DATE, ticker, startDateParam);
        Request request = new Request.Builder().url(requestUrl).build();

        Response response = client.newCall(request).execute();
        if (response.body() != null) {
            String responseStr = response.body().string();
            JSONObject responseObj = new JSONObject(responseStr);
            ErrorHandling.validateTicker(responseObj);
            JSONObject datasetObject = (JSONObject) responseObj.get("dataset");
            String newestAvailDate = (String) datasetObject.get("newest_available_date");
            String oldestAvailDate = (String) datasetObject.get("oldest_available_date");
            return new String[] {newestAvailDate, oldestAvailDate};
        }
        return null;
    }

    @Override
    public void getDefaultFirstDateData(String ticker, String firstDate) throws IOException {
        String requestUrl = String.format(GET_200_DAY_MOVING_AVG, ticker, firstDate);
        Request request = new Request.Builder().url(requestUrl).build();

        Response response = client.newCall(request).execute();
        if (response.body() != null) {
            String responseStr = response.body().string();
            JSONObject responseObj = new JSONObject(responseStr);
            JSONObject datasetObject = (JSONObject) responseObj.get("dataset");
            defaultFirstDateDataJSONArray = (JSONArray) datasetObject.get("data");
        }
    }

    @Override
    public JSONArray getDefaultFirstDateDataJSONArray() {
        return defaultFirstDateDataJSONArray;
    }


}
