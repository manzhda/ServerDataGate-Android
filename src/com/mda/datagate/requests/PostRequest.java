package com.mda.datagate.requests;

import android.content.Context;
import com.mda.datagate.AbstractRequest;
import com.mda.datagate.NetCommandListener;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

public abstract class PostRequest<T> extends AbstractRequest<T> {
    public PostRequest(Context context, NetCommandListener listener) {
        super(context, listener);
    }

    @Override
    protected HttpRequestBase createHttpRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        return httpPost;
    }
}
