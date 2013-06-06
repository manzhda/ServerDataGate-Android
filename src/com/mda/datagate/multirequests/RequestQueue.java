package com.mda.datagate.multirequests;

import com.mda.datagate.NetCommandListener;
import com.mda.datagate.Request;
import com.mda.datagate.Status;

public class RequestQueue extends MultiRequest {
    public RequestQueue() {
        super(null);
    }

    public RequestQueue(NetCommandListener listener) {
        super(listener);
    }

    @Override
    public void call() {
        if (getRequests().isEmpty()) {
            getListener().onComplete(null, null);
            return;
        }

        Request firstRequest = getRequests().get(0);
        wrapRequestListener(firstRequest);

        for (int i = 1; i < getRequests().size(); ++i) {
            Request request = getRequests().get(i);
            wrapRequestListener(request);

            Request prevRequest = getRequests().get(i - 1);
            HelperListener helperListener = (HelperListener) prevRequest.getListener();
            helperListener.setNextRequest(request);
        }

        firstRequest.call();
    }

    public void wrapRequestListener(Request request) {
        NetCommandListener requestListener = request.getListener();
        HelperListener listener = new HelperListener(requestListener);
        request.setListener(listener);
    }

    private class HelperListener implements NetCommandListener {
        private final NetCommandListener mRequestListener;
        private Request mNextRequest;

        HelperListener(NetCommandListener requestListener) {
            mRequestListener = requestListener;
        }

        public void setNextRequest(Request nextRequest) {
            mNextRequest = nextRequest;
        }

        @Override
        public void onComplete(Request request, Object response) {
            if (mRequestListener != null) {
                mRequestListener.onComplete(request, response);
            }
            if (mNextRequest != null) {
                mNextRequest.call();
            } else if (getListener() != null) {
                getListener().onComplete(request, response);
            }
        }

        @Override
        public boolean onError(Status error, Object responseObj) {
            if (mRequestListener != null) {
                mRequestListener.onError(error, responseObj);
            }
            if (getListener() != null) {
                getListener().onError(error, responseObj);
            }
            return false;
        }
    }
}
