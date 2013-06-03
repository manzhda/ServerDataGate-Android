package com.mda.datagate;

import android.content.Context;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class DeleteRequest<T> extends AbstractRequest<T> {
    protected DeleteRequest(Context context, NetCommandListener listener) {
        super(context, listener);
    }

    @Override
    protected HttpRequestBase createHttpRequest(String url) {
        HttpDelete httpGet = new HttpDelete(url);
        httpGet.setHeader("Accept", "application/json");
        return httpGet;
    }
}
