package com.mda.datagate;

public class DataGateResponse {
    private final int mStatusCode;
    private final String mResponseString;

    public DataGateResponse(int statusCode, String responseString) {
        mStatusCode = statusCode;
        mResponseString = responseString;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public String getResponseString() {
        return mResponseString;
    }
}
