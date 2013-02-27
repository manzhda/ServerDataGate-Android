package com.mda.datagate;

import android.content.Context;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest<T> {
    private final Context mContext;
    private NetCommandListener mListener;

    protected AbstractRequest(Context context, NetCommandListener listener) {
        mContext = context;
        mListener = listener;
    }

    public NetCommandListener getListener() {
        return mListener;
    }

    public void setListener(NetCommandListener listener) {
        mListener = listener;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract HttpRequestBase createHttpRequest(String url);

    protected abstract String getPath();

    protected Map<String, String> getPathParams() {
        return new HashMap<String, String>();
    }

    public abstract T parse(String responseString) throws JSONException;

    public HttpRequestBase getHttpRequest() {
        String path = getPath();
        HttpRequestBase httpRequest = createHttpRequest(path);
        return httpRequest;
    }
}
