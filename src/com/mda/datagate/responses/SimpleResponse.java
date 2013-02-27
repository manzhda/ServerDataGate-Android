package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

public class SimpleResponse implements ErrorContainer {
    @XmlElement(name="error")
    private RequestError mError;

    @Override
    public RequestError getError() {
        return mError;
    }
}
