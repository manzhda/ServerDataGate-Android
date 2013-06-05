package com.mda.datagate;

import android.os.AsyncTask;
import com.mda.datagate.utils.MyLog;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.LinkedList;

public class Controller {
    private static final String TAG = Controller.class.getSimpleName();

    private final Collection<AbstractRequest> mRequests = new LinkedList<AbstractRequest>();

    public void callRequest(AbstractRequest request) {
        mRequests.add(request);
        makeRequest(request);
    }

    public void stopAllRequests() {
        for (AbstractRequest request : mRequests) {
            abortRequest(request);
        }
        mRequests.clear();
    }

    public void stopRequest(AbstractRequest request) {
        abortRequest(request);
        mRequests.remove(request);
    }

    private void abortRequest(AbstractRequest request) {
        request.getHttpRequest().abort();
        if (request.getHttpClient() != null) {
            request.getHttpClient().getConnectionManager().shutdown();
        }
    }

    public void makeRequest(AbstractRequest request) {
        RequestAsyncTask task = new RequestAsyncTask();
        task.execute(request);
    }

    private class RequestAsyncTask extends AsyncTask<AbstractRequest, Void, RequestResponseContainer> {
        @Override
        protected RequestResponseContainer doInBackground(AbstractRequest... requests) {
            if (!DataGate.isInternetPresent(requests[0].getContext())) {
                return new RequestResponseContainer(requests[0],
                        new Response(com.mda.datagate.Status.NO_INTERNET_CONNECTION));
            }
            return Controller.this.execute(requests[0]);
        }

        @Override
        protected void onPostExecute(RequestResponseContainer result) {
            AbstractRequest request = result.getRequest();
            Response response = result.getResponse();
            com.mda.datagate.Status responseCode = response.getCode();
            NetCommandListener listener = request.getListener();
            if (listener == null) {
                return;
            }

            if (responseCode == com.mda.datagate.Status.OK) {
                listener.onComplete(request, response.getData());
            } else {
                listener.onError(responseCode, response.getData());
            }
        }
    }

    RequestResponseContainer execute(AbstractRequest request) {
        try {
            DataGateResponse response = getResponse(request);

            switch (response.getStatusCode()) {
                case HttpStatus.SC_OK:
                case HttpStatus.SC_CREATED:
                case HttpStatus.SC_UNPROCESSABLE_ENTITY: {
                    Object data = request.parse(response.getResponseString());
                    return new RequestResponseContainer(request, new Response(Status.OK, data));
                }

                case HttpStatus.SC_UNAUTHORIZED:
                    Object data = request.parse(response.getResponseString());
                    return new RequestResponseContainer(request, new Response(Status.NOT_AUTHORITY, data));

                default:
                    return new RequestResponseContainer(request, new Response(Status.DATA_UNAVAILABLE));
            }
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            return new RequestResponseContainer(request, new Response(Status.CONNECTION_TIMEOUT));
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return new RequestResponseContainer(request, new Response(Status.CONNECTION_TIMEOUT));
        } catch (HttpHostConnectException ex) {
            HttpRequestBase httpRequest = request.getHttpRequest();
            MyLog.vt(TAG, "Request aborted: ", httpRequest.getMethod(), httpRequest.getURI());
            return new RequestResponseContainer(request, new Response(Status.REQUEST_ABORTED));
        } catch (IOException ex) {
            HttpRequestBase httpRequest = request.getHttpRequest();
            MyLog.vt(TAG, "Request aborted: ", httpRequest.getMethod(), httpRequest.getURI());
            return new RequestResponseContainer(request, new Response(Status.REQUEST_ABORTED));
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResponseContainer(request, new Response(Status.DATA_UNAVAILABLE));
        }
    }

    private DataGateResponse getResponse(AbstractRequest request) throws IOException {
        HttpRequestBase httpRequest = request.getHttpRequest();
        MyLog.vt(TAG, "Call request: ", httpRequest.getMethod(), httpRequest.getURI());

        DataGateResponse response = DataGate.request(request, httpRequest);
        MyLog.vt(TAG, "Request: ", httpRequest.getMethod(), httpRequest.getURI());
        MyLog.vt(TAG, "Response status code:", response.getStatusCode());
        MyLog.vt(TAG, "Response:", response.getResponseString());

        mRequests.remove(request);

        return response;
    }
}
