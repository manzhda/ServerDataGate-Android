package com.mda.datagate.requests;

import android.content.Context;
import com.mda.datagate.AbstractRequest;
import com.mda.datagate.NetCommandListener;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class GetRequest<T> extends AbstractRequest<T> {
    public GetRequest(Context context, NetCommandListener listener) {
        super(context, listener);
    }

    @Override
    protected HttpRequestBase createHttpRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");
        return httpGet;
    }
}
