package com.mda.datagate;

public interface NetCommandListener {
    public void onComplete(Request request, Object response);

    // TODO boolean => void
    public boolean onError(Status error, Object responseObj);
}
