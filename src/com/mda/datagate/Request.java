package com.mda.datagate;

public interface Request {
    public void call();
    public void abort();

    public void setListener(NetCommandListener listener);
    public NetCommandListener getListener();

    public boolean isNeedReturnHeader();
    public boolean needToRetry();
}
