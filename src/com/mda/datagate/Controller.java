package com.mda.datagate;

import com.mda.datagate.utils.MyLog;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class Controller {
    private static final String TAG = Controller.class.getSimpleName();

    public static RequestResponseContainer execute(AbstractRequest request) {
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

    private static DataGateResponse getResponse(AbstractRequest request) throws IOException {
        HttpRequestBase httpRequest = request.getHttpRequest();
        MyLog.vt(TAG, "Call request: ", httpRequest.getMethod(), httpRequest.getURI());

        DataGateResponse response = DataGate.request(request, httpRequest);
        MyLog.vt(TAG, "Request: ", httpRequest.getMethod(), httpRequest.getURI());
        MyLog.vt(TAG, "Response status code:", response.getStatusCode());
        MyLog.vt(TAG, "Response:", response.getResponseString());

        RequestAborter requestAborter = request.getRequestAborter();
        if (requestAborter != null) {
            requestAborter.remove(request);
        }

        return response;
    }
}
