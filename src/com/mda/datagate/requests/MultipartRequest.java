package com.mda.datagate.requests;

import android.content.Context;
import com.mda.datagate.AbstractRequest;
import com.mda.datagate.NetCommandListener;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;

public abstract class MultipartRequest<T> extends AbstractRequest<T> {
    private final JSONRequest.Type mType;

    public MultipartRequest(Context context, NetCommandListener listener, JSONRequest.Type type) {
        super(context, listener);
        mType = type;
    }

    @Override
    protected HttpRequestBase createHttpRequest(String url) {
        HttpEntityEnclosingRequestBase httpRequest = null;
        // TODO: code duplication
        switch (mType) {
            case POST:
                httpRequest = new HttpPost(url);
                break;

            case PUT:
                httpRequest = new HttpPut(url);
                break;
        }

        httpRequest.addHeader("Accept", "application/json");
        MultipartEntity entity = createMultipartEntity();
        httpRequest.setEntity(entity);
        return httpRequest;
    }

    protected abstract MultipartEntity createMultipartEntity();
}
