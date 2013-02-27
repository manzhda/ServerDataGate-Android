package com.mda.datagate.requests;

import android.content.Context;
import biz.kulik.android.jaxb.library.Annotations.XmlElement;
import biz.kulik.android.jaxb.library.parser.Parser;
import biz.kulik.android.jaxb.library.parser.ParserImpl;
import biz.kulik.android.jaxb.library.parser.UnMarshalerTypes;
import com.mda.datagate.Config;
import com.mda.datagate.NetCommandListener;
import com.mda.datagate.PostRequest;
import com.mda.datagate.responses.ErrorContainer;
import com.mda.datagate.responses.RequestError;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class GetBookRequest extends PostRequest<GetBookRequest.Response> {
    private static final String URL = Config.REQUEST_BASE_URL + "getBook";

    private String mSession;
    private long mBookId;

    public GetBookRequest(Context context, NetCommandListener listener, String session, long bookId) {
        super(context, listener);
        mSession = session;
        mBookId = bookId;
    }

    @Override
    protected Map<String, String> getPathParams() {
        Map<String, String> params = new HashMap<String, String>(2);
        params.put("sessionId", mSession);
        params.put("bookId", String.valueOf(mBookId));
        return params;
    }

    @Override
    protected String getPath() {
        Map<String, String> params = getPathParams();
        return URL + ParamsComposer.compose(params);
    }

    @Override
    public Response parse(String responseString) throws JSONException {
        Parser parser = new ParserImpl(UnMarshalerTypes.JSONAdapter);
        Response response = parser.parse(Response.class, responseString);
        return response;
    }

    public static class Response implements ErrorContainer {
        @XmlElement(name = "resource")
        public GetBook mGetBookResponse;

        @XmlElement(name="error")
        private RequestError mError;

        @Override
        public RequestError getError() {
            return mError;
        }
    }

    public static class GetBook {
        @XmlElement(name = "bookURL")
        public String mBookUrl;

        @XmlElement(name = "bookFileSizeInByte")
        public Long mBookFileSizeInByte;

        @XmlElement(name = "bookMD5")
        public String mBookMD5;
    }
}
