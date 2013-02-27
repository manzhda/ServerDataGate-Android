package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

import java.math.BigDecimal;

public class AccountResponse {
    @XmlElement(name = "email")
    public String mEmail;

    @XmlElement(name = "username")
    public String mName;

    @XmlElement(name = "balance")
    public BigDecimal mBalance;
}