package com.mda.datagate.multirequests;

import com.mda.datagate.NetCommandListener;
import com.mda.datagate.Request;
import com.mda.datagate.Status;

import java.util.ArrayList;
import java.util.List;

public class RequestGroup extends MultiRequest {
    protected int mRequestsAmount;
    private final List<Status> mStatuses = new ArrayList<Status>();

    public RequestGroup() {
        super(null);
    }

    public RequestGroup(NetCommandListener listener) {
        super(listener);
    }

    void onResult(Status status) {
        --mRequestsAmount;
        mStatuses.add(status);
        if (mRequestsAmount == 0) {
            if (getListener() != null) {
                getListener().onComplete(null, null);
            }
        }
    }

    public List<Status> getStatuses() {
        return mStatuses;
    }

    @Override
    public void call() {
        if (getRequests().isEmpty()) {
            getListener().onComplete(null, null);
            return;
        }

        mRequestsAmount = getRequests().size();
        for (Request request : getRequests()) {
            wrapRequestListener(request);
            request.call();
        }
    }

    protected void wrapRequestListener(Request request) {
        NetCommandListener requestListener = request.getListener();
        HelperListener listener = new HelperListener(this, requestListener);
        request.setListener(listener);
    }

    private static class HelperListener implements NetCommandListener {
        private final NetCommandListener mListener;
        private final RequestGroup mRequestCounter;

        private HelperListener(RequestGroup requestCounter, NetCommandListener nextListener) {
            mRequestCounter = requestCounter;
            mListener = nextListener;
        }

        @Override
        public void onComplete(Request request, Object response) {
            if (mListener != null) {
                mListener.onComplete(request, response);
            }
            mRequestCounter.onResult(Status.OK);
        }

        @Override
        public boolean onError(Status error, Object responseObj) {
            if (mListener != null) {
                mListener.onError(error, responseObj);
            }
            mRequestCounter.onResult(error);
            return false;
        }
    }
}
