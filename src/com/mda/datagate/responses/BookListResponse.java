package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

import java.util.List;

public class BookListResponse {
    @XmlElement(name = "bookCount")
    public Integer mCount;

    @XmlElement(name = "bookArray")
    public List<StoreBookResponse> mBooks;
}
