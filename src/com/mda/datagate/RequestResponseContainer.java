package com.mda.datagate;

public class RequestResponseContainer {
    AbstractRequest mRequest;
    Response mResponse;

    public RequestResponseContainer(AbstractRequest request, Response response) {
        mRequest = request;
        mResponse = response;
    }

    public AbstractRequest getRequest() {
        return mRequest;
    }

    public Response getResponse() {
        return mResponse;
    }
}
