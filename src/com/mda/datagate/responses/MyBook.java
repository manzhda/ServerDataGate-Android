package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

public class MyBook {
    public long mId;

    @XmlElement(name = "bookId")
    public final Long mServerId;

    @XmlElement(name = "title")
    public String mTitle;

    @XmlElement(name = "bookImage")
    public String mImageUrl;

    @XmlElement(name = "author")
    public String mAuthor;

    @XmlElement(name = "category")
    public String mCategory;

    public String mUrl;

    public MyBook() {
        mServerId = 0L;
    }

    public MyBook(Long serverId) {
        mServerId = serverId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof MyBook)) {
            return false;
        }

        MyBook book = (MyBook) object;
        return mServerId.equals(book.mServerId);
    }

    @Override
    public int hashCode() {
        return mServerId.hashCode();
    }
}
