package com.mda.datagate;

import java.util.Collection;

public enum Status {
    OK,
    NOT_AUTHORITY,
    DATA_UNAVAILABLE,
    NO_INTERNET_CONNECTION,
    REQUEST_ABORTED,
    CONNECTION_TIMEOUT;

    public static Status getWorst(Collection<Status> statuses) {
        if (statuses.contains(NO_INTERNET_CONNECTION)) {
            return NO_INTERNET_CONNECTION;
        } else if (statuses.contains(DATA_UNAVAILABLE)) {
            return DATA_UNAVAILABLE;
        } else {
            return OK;
        }
    }
}
