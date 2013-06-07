package com.mda.datagate.multirequests;

import com.mda.datagate.NetCommandListener;
import com.mda.datagate.Request;

import java.util.ArrayList;
import java.util.List;

public abstract class MultiRequest implements Request {
    private final List<Request> mRequests = new ArrayList<Request>();
    private NetCommandListener mListener;

    public MultiRequest(NetCommandListener listener) {
        mListener = listener;
    }

    public void addRequest(Request request) {
        mRequests.add(request);
    }

    protected List<Request> getRequests() {
        return mRequests;
    }

    @Override
    public NetCommandListener getListener() {
        return mListener;
    }

    @Override
    public void setListener(NetCommandListener listener) {
        mListener = listener;
    }

    @Override
    public void abort() {
        for (Request request : mRequests) {
            request.abort();
        }
    }

    @Override
    public boolean isNeedReturnHeader() {
        return false;
    }

    @Override
    public boolean needToRetry() {
        return false;
    }
}
