package com.tuanle.stockintelligence.handler;

import com.tuanle.stockintelligence.exception.InvalidParameterException;
import org.json.JSONObject;

public class ErrorHandling {

    public static final String INVALID_TICKER_SYMBOL_ERROR_MESSAGE = "Invalid ticker symbol";

    public static final String INVALID_DATE_ERROR_MESSAGE = "Invalid start-date and end-date";

    public static final String SUGGESTED_AVAIL_START_DATE_MESSAGE = "There is no data for this 'start_date'. Newest available date is '%s', oldest available date is '%s'";

    public static final String INVALID_TICKER_CODE = "QECx02";

    public static final String INVALID_DATE = "QESx04";

    private static boolean isDateValid(JSONObject responseObj) {
        if (responseObj.has("quandl_error")) {
            JSONObject errorObject = (JSONObject) responseObj.get("quandl_error");
            String errorCode = errorObject.getString("code");
            return !errorCode.equals(INVALID_DATE);
        }
        return true;
    }

    public static void validateDate(JSONObject responseObj) throws InvalidParameterException {
        if (!isDateValid(responseObj)) {
            throw new InvalidParameterException(INVALID_DATE_ERROR_MESSAGE);
        }
    }

    private static boolean isResponseValid(JSONObject responseObj) {
        if (responseObj.has("quandl_error")) {
            JSONObject errorObject = (JSONObject) responseObj.get("quandl_error");
            String errorCode = errorObject.getString("code");
            return !errorCode.equals(INVALID_TICKER_CODE);
        }
        return true;
    }

    public static void validateTicker(JSONObject responseObj) throws InvalidParameterException {
        if (!isResponseValid(responseObj)) {
            throw new InvalidParameterException(INVALID_TICKER_SYMBOL_ERROR_MESSAGE);
        }
    }

    public static boolean isTickerValid(JSONObject responseObj) {
        return isResponseValid(responseObj);
    }

}
