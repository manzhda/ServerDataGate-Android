package com.mda.datagate;

import java.util.Collection;
import java.util.LinkedList;

public class RequestAborter {
    private final Collection<Request> mRequests = new LinkedList<Request>();

    public void callRequest(Request request) {
        mRequests.add(request);
        request.call();
    }

    public void stopAllRequests() {
        for (Request request : mRequests) {
            request.abort();
        }
        mRequests.clear();
    }

    public void stopRequest(Request request) {
        request.abort();
        mRequests.remove(request);
    }

    public void remove(Request request) {
        mRequests.remove(request);
    }
}
