package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

public class SessionResponse {
    @XmlElement(name = "sessionId")
    public String mSession;
}
