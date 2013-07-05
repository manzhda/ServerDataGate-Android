package com.mda.datagate.requests;

import android.content.Context;
import com.mda.datagate.AbstractRequest;
import com.mda.datagate.Controller;
import com.mda.datagate.NetCommandListener;
import com.mda.datagate.utils.MyLog;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public abstract class JSONRequest<T> extends AbstractRequest<T> {
    private static final String TAG = Controller.class.getSimpleName();

    private static final String CONTENT_TYPE_JSON = "application/json";

    public enum Type {
        POST,
        PUT
    }

    private final Type mType;

    public JSONRequest(Context context, NetCommandListener listener, Type type) {
        super(context, listener);
        mType = type;
    }

    protected abstract JSONObject getJsonBody();

    @Override
    protected HttpRequestBase createHttpRequest(String url) {
        HttpEntityEnclosingRequestBase httpRequest = null;
        switch (mType) {
            case POST:
                httpRequest = new HttpPost(url);
                break;

            case PUT:
                httpRequest = new HttpPut(url);
                break;
        }
        addJsonEntity(httpRequest);
        httpRequest.setHeader("Accept", CONTENT_TYPE_JSON);
        httpRequest.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON);
        return httpRequest;
    }

    protected void addJsonEntity(HttpEntityEnclosingRequest httpRequest){
        JSONObject body = getJsonBody();

        if (body == null) {
            return;
        }

        try {
            StringEntity strBody = new StringEntity(body.toString(), "utf-8");
            httpRequest.setEntity(strBody);
            MyLog.vt(TAG, body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
