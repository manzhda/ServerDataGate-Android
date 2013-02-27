package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

public class Category {
    @XmlElement(name = "categoryId")
    public Long mId;

    @XmlElement(name = "name")
    public String mName;

    @Override
    public String toString() {
        return mName;
    }
}
