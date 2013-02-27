package com.mda.datagate.responses;

import biz.kulik.android.jaxb.library.Annotations.XmlElement;

public class RequestError {
    private static final int NO_ERROR = -1;

    public static final int VALIDATION_FAILED = 2;
    public static final int BALANCE_LESS_THAN_PRICE = 16;
    public static final int BOOK_IS_ENCRYPTING_NOW = 18;
    public static final int HAVE_NO_BOOKS = 24;
    public static final int NOTHING_FOUND = 28;

    @XmlElement(name = "errorCode")
    public Integer mCode;

    @XmlElement(name = "errorDescription")
    public String mDescription;

    @XmlElement(name = "resource")
    public Resource mResource;

    public boolean isInvalid() {
        return (mCode == null);
    }

    public void init() {
        if (isInvalid()) {
            mCode = NO_ERROR;
        }

        if (mResource != null) {
            mResource.mSession = getResourceString(mResource.mSession);
            mResource.mPassword = getResourceString(mResource.mPassword);
            mResource.mEmail = getResourceString(mResource.mEmail);
        }
    }

    private static String getResourceString(String str) {
        if (str == null) {
            return null;
        } else if (str.length() > 0) {
            return str.replace("[\"", "").replace("\"]", "").replace("\\\"", "\"");
        } else {
            return null;
        }
    }

    public boolean noError() {
        return mCode == NO_ERROR;
    }

    public boolean isSessionInvalid() {
        return (mCode == VALIDATION_FAILED) && (mResource != null) && (mResource.mSession != null);
    }

    public boolean isPasswordInvalid() {
        return (mCode == VALIDATION_FAILED) && (mResource != null) && (mResource.mPassword != null);
    }

    public boolean isEmailInvalid() {
        return (mCode == VALIDATION_FAILED) && (mResource != null) && (mResource.mEmail != null);
    }

    public boolean isNothingFound() {
        return (mCode == NOTHING_FOUND);
    }

    public boolean isHaveNoBooks() {
        return (mCode == HAVE_NO_BOOKS);
    }

    public static class Resource {
        @XmlElement(name = "sessionId")
        public String mSession;

        @XmlElement(name = "password")
        public String mPassword;

        @XmlElement(name = "email")
        public String mEmail;
    }
}
