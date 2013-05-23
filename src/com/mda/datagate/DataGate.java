package com.mda.datagate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataGate {
    private static final int mConnectionTimeout = 30000;
    private static final int mSoTimeout = 30000;

    private DataGate() {
    }

    public static DataGateResponse request(AbstractRequest request, HttpRequestBase httpRequest) throws IOException {
        HttpResponse httpResponse = execute(request, httpRequest);

        String responseString = getResponseString(httpResponse);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        DataGateResponse dataGateResponse = new DataGateResponse(statusCode, responseString);
        return dataGateResponse;
    }

    private static HttpResponse execute (AbstractRequest request, HttpRequestBase httpRequest) throws IOException {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, mConnectionTimeout);
        HttpConnectionParams.setSoTimeout(httpParameters, mSoTimeout);
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
        request.setHttpClient(httpClient);
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        return httpResponse;
    }

    private static String getResponseString(HttpResponse response) throws IOException {
        InputStream responseIS = response.getEntity().getContent();
        BufferedReader responseBR = new BufferedReader(new InputStreamReader(responseIS, "UTF-8"));
        StringBuilder responseSB = new StringBuilder();

        String line;
        while ((line = responseBR.readLine()) != null) {
            responseSB.append(line).append("\n");
        }
        responseBR.close();
        responseIS.close();

        String responseString = responseSB.toString().trim();
        return responseString;
    }

    public static boolean isInternetPresent(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return !(info == null || !info.isConnected() || !info.isAvailable());
    }
}
