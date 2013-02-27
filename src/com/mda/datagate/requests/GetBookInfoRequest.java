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

public class GetBookInfoRequest extends PostRequest<GetBookInfoRequest.Response> {
    private static final String URL = Config.REQUEST_BASE_URL + "getBookInfo";

    private final long mBookId;

    public GetBookInfoRequest(Context context, NetCommandListener listener, long bookId) {
        super(context, listener);
        mBookId = bookId;
    }

    @Override
    protected String getPath() {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("bookId", String.valueOf(mBookId));
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
        public BookInfo mBookInfo;

        @XmlElement(name = "error")
        private RequestError mError;

        @Override
        public RequestError getError() {
            return mError;
        }
    }

    public static class BookInfo {
        @XmlElement(name = "description")
        public String mDescription;

        @XmlElement(name = "bookPreviewURL")
        public String mBookPreviewUrl;
    }
}
