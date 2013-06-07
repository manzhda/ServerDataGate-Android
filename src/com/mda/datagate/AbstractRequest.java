package com.mda.datagate;

import android.content.Context;
import android.os.AsyncTask;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest<T> implements Request {
    private final Context mContext;
    private HttpRequestBase mHttpRequest;
    private HttpClient mHttpClient;
    private NetCommandListener mListener;
    private RequestAborter mRequestAborter;

    protected AbstractRequest(Context context, NetCommandListener listener) {
        mContext = context;
        mListener = listener;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public boolean isNeedReturnHeader() {
        return false;
    }

    @Override
    public NetCommandListener getListener() {
        return mListener;
    }

    @Override
    public void setListener(NetCommandListener listener) {
        mListener = listener;
    }

    public RequestAborter getRequestAborter() {
        return mRequestAborter;
    }

    public void setRequestAborter(RequestAborter requestAborter) {
        mRequestAborter = requestAborter;
    }

    protected abstract HttpRequestBase createHttpRequest(String url);

    protected abstract String getPath();

    protected Map<String, String> getPathParams() {
        return new HashMap<String, String>();
    }

    public abstract T parse(String responseString) throws JSONException;

    public HttpRequestBase getHttpRequest() {
        if (mHttpRequest != null) {
            return mHttpRequest;
        }

        String path = getPath();
        mHttpRequest = createHttpRequest(path);
        return mHttpRequest;
    }

    public void setHttpClient(HttpClient httpClient) {
        mHttpClient = httpClient;
    }

    @Override
    public void abort() {
        getHttpRequest().abort();
        if (mHttpClient != null) {
            mHttpClient.getConnectionManager().shutdown();
        }
    }

    @Override
    public void call() {
       RequestExecuter.executeRequest(this);
    }

    @Override
    public boolean needToRetry() {
        return false;
    }
}
