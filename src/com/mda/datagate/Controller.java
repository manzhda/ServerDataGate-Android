package com.mda.datagate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import com.mda.datagate.utils.MyLog;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;

public class Controller {
    private static final String TAG = Controller.class.getSimpleName();

    private Controller() {
    }

    public static AsyncTask<AbstractRequest, Void, RequestResponseContainer> makeRequest(AsyncTaskCallback callback, AbstractRequest request) {
        return new RequestAsyncTask(callback).execute(request);
    }

    private static class RequestAsyncTask extends AsyncTask<AbstractRequest, Void, RequestResponseContainer> {
        private AsyncTaskCallback mCallback;

        RequestAsyncTask(AsyncTaskCallback callback) {
            mCallback = callback;
        }

        @Override
        protected RequestResponseContainer doInBackground(AbstractRequest... requests) {
            if (!isInternetPresent(requests[0].getContext())) {
                return new RequestResponseContainer(requests[0],
                        new Response(com.mda.datagate.Status.NO_INTERNET_CONNECTION));
            }
            return Controller.execute(requests[0]);
        }

        @Override
        protected void onPostExecute(RequestResponseContainer result) {
            mCallback.done(result.getRequest(), result.getResponse());
        }
    }

    static RequestResponseContainer execute(AbstractRequest request) {
        try {
            DataGateResponse response = getResponse(request);

            switch (response.getStatusCode()) {
                case HttpStatus.SC_OK:
                case HttpStatus.SC_CREATED:
                case HttpStatus.SC_UNPROCESSABLE_ENTITY:
                    Object data = request.parse(response.getResponseString());
                    return new RequestResponseContainer(request, new Response(Status.OK, data));

                case HttpStatus.SC_UNAUTHORIZED:
                    return new RequestResponseContainer(request, new Response(Status.NOT_AUTHORITY));

                default:
                    return new RequestResponseContainer(request, new Response(Status.DATA_UNAVAILABLE));
            }
        } catch (HttpHostConnectException ex) {
            ex.printStackTrace();
            return new RequestResponseContainer(request, new Response(Status.NO_INTERNET_CONNECTION));
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResponseContainer(request, new Response(Status.DATA_UNAVAILABLE));
        }
    }

    private static DataGateResponse getResponse(AbstractRequest request) throws IOException {
        HttpRequestBase httpRequest = request.getHttpRequest();
        MyLog.vt(TAG, "Call request: ", httpRequest.getMethod(), httpRequest.getURI());

        DataGateResponse response = DataGate.request(httpRequest);
        MyLog.vt(TAG, "Request: ", httpRequest.getMethod(), httpRequest.getURI());
        MyLog.vt(TAG, "Response status code:", response.getStatusCode());
        MyLog.vt(TAG, "Response:", response.getResponseString());

        return response;
    }

    public static boolean isInternetPresent(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        return !(info == null || !info.isConnected() || !info.isAvailable());
    }

    public static interface AsyncTaskCallback {
        public void done(AbstractRequest request, Response response);
    }
}
