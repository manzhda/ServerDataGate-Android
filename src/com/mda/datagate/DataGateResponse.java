package com.mda.datagate;

import org.apache.http.Header;

import java.util.HashMap;

public class DataGateResponse {
    private final int mStatusCode;
    private final String mResponseString;
    private HashMap<String, String> mHeadersList;

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

    public HashMap<String, String> getHeadersList() {
        return mHeadersList;
    }

    public void setHeadersList(org.apache.http.Header[] list){
         mHeadersList = new HashMap<String, String>();
         for(int i = 0; i<list.length; i++){
         mHeadersList.put(list[i].getName(), list[i].getValue());
         }
    }
}
