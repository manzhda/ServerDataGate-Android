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
        RequestAsyncTask task = new RequestAsyncTask();
        task.execute(this);
    }

    private static class RequestAsyncTask extends AsyncTask<AbstractRequest, Void, RequestResponseContainer> {
        @Override
        protected RequestResponseContainer doInBackground(AbstractRequest... requests) {
            if (!DataGate.isInternetPresent(requests[0].getContext())) {
                return new RequestResponseContainer(requests[0],
                        new Response(com.mda.datagate.Status.NO_INTERNET_CONNECTION));
            }
            return Controller.execute(requests[0]);
        }

        @Override
        protected void onPostExecute(RequestResponseContainer result) {
            AbstractRequest request = result.getRequest();
            Response response = result.getResponse();
            com.mda.datagate.Status responseCode = response.getCode();

            NetCommandListener listener = request.getListener();
            if (listener != null) {
                if (responseCode == com.mda.datagate.Status.OK) {
                    listener.onComplete(request, response.getData());
                } else {
                    listener.onError(responseCode, response.getData());
                }
            }
        }
    }
}
