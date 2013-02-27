package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

import java.io.Serializable;
import java.math.BigDecimal;

public class StoreBookResponse implements Serializable {
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

    @XmlElement(name = "price")
    public BigDecimal mPrice;

    @XmlElement(name = "currency")
    public String mCurency;

    @XmlElement(name = "downloadCount")
    public Integer mDownloadCount;

    @XmlElement(name = "userBoughtThisBook")
    public Boolean mIsBought;


    public StoreBookResponse() {
        mServerId = 0L;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof StoreBookResponse)) {
            return false;
        }

        StoreBookResponse book = (StoreBookResponse) object;
        return mServerId.equals(book.mServerId);
    }

    @Override
    public int hashCode() {
        return mServerId.hashCode();
    }
}
