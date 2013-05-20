package com.mda.datagate;

public interface NetCommandListener {
    public void onComplete(AbstractRequest request, Object response);
    public boolean onError(Status errorId, Object responseObj);
}
