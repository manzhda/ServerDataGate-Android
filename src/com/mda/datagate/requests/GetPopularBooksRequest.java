package com.mda.datagate.requests;

import android.content.Context;
import com.mda.datagate.Config;
import com.mda.datagate.NetCommandListener;

import java.util.Map;

public class GetPopularBooksRequest extends GetBookRequest {
    private static final String URL = Config.REQUEST_BASE_URL + "getPopularBooks";

    public GetPopularBooksRequest(Context context, NetCommandListener listener, String session, int offset) {
        super(context, listener, session, offset);
    }

    @Override
    protected String getPath() {
        Map<String, String> params = getPathParams();
        return URL + ParamsComposer.compose(params);
    }
}
