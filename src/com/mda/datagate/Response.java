package com.mda.datagate;

public class Response {
    private Status mCode = Status.OK;
    private Object mData;

    public Response(Status code, Object data) {
        mCode = code;
        mData = data;
    }

    public Response(Status code) {
        mCode = code;
    }

    public Response() {
    }

    public Object getData() {
        return mData;
    }

    public Status getCode() {
        return mCode;
    }
}
