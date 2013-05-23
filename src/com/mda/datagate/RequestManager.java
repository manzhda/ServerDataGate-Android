package com.mda.datagate;

public class RequestManager {
    public void callRequest(AbstractRequest request) {
        Controller.callRequest(request);
    }
}
