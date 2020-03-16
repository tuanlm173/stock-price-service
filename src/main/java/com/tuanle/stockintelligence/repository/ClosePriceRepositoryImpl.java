package com.tuanle.stockintelligence.repository;

import com.tuanle.stockintelligence.config.GeneralConfig;
import com.tuanle.stockintelligence.exception.InvalidParameterException;
import com.tuanle.stockintelligence.handler.ErrorHandling;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ClosePriceRepositoryImpl implements ClosePriceRepository {

    private static final String GET_CLOSE_PRICE = GeneralConfig.API_URL + "%s.json?start_date=%s&end_date=%s&column_index=4&api_key=" + GeneralConfig.API_KEY;

    private static final String GET_200_DAY_MOVING_AVG =
            GeneralConfig.API_URL + "%s.json?start_date=%s&column_index=4&collapse=daily&order=asc&limit=200&api_key=" + GeneralConfig.API_KEY;


    private final OkHttpClient client;

    private JSONArray closePriceJSONArray = null;
    private JSONArray twoHundredDayPriceJSONArray = null;

    @Autowired
    public ClosePriceRepositoryImpl(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public void getListOfClosePriceFromSource(String ticker, String startDate, String endDate) throws InvalidParameterException, IOException {
        String requestUrl = String.format(GET_CLOSE_PRICE, ticker, startDate, endDate);
        Request request = new Request.Builder().url(requestUrl).build();

        Response response = client.newCall(request).execute();
        if(response.body() != null) {
            String responseStr = response.body().string();
            JSONObject responseObj = new JSONObject(responseStr);
            ErrorHandling.validateTicker(responseObj);
            ErrorHandling.validateDate(responseObj);
            JSONObject datasetObject = (JSONObject) responseObj.get("dataset");
            closePriceJSONArray = (JSONArray) datasetObject.get("data");
        }
    }

    @Override
    public void getListOf200PriceFromSource(String ticker, String startDate) throws InvalidParameterException, IOException {
        String requestUrl = String.format(GET_200_DAY_MOVING_AVG, ticker, startDate);
        Request request = new Request.Builder().url(requestUrl).build();

        Response response = client.newCall(request).execute();
        if (response.body() != null) {
            String responseStr = response.body().string();
            JSONObject responseObj = new JSONObject(responseStr);
            ErrorHandling.validateTicker(responseObj);
            ErrorHandling.validateDate(responseObj);
            JSONObject datasetObject = (JSONObject) responseObj.get("dataset");
            twoHundredDayPriceJSONArray = (JSONArray) datasetObject.get("data");
        }
    }

    public JSONArray getClosePriceJSONArray() {
        return closePriceJSONArray;
    }

    public JSONArray getListOf200PriceJSONArray() {
        return twoHundredDayPriceJSONArray;
    }

    public String getClosePriceUrl() {
        return GET_CLOSE_PRICE;
    }

    public String get200DayMovingAverageUrl() {
        return GET_200_DAY_MOVING_AVG;
    }

}
