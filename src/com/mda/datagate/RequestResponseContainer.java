package com.mda.datagate;

import java.util.HashMap;

public class RequestResponseContainer {
    AbstractRequest mRequest;
    Response mResponse;
    HashMap<String, String> mHeadersList;

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

    public HashMap<String, String> getHeadersList() {
        return mHeadersList;
    }

    public void setHeadersList(HashMap<String, String> headersList) {
        mHeadersList = headersList;
    }

}
