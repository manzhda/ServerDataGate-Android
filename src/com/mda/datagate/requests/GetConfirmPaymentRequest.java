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

import java.util.Map;

public class GetConfirmPaymentRequest extends PostRequest<GetConfirmPaymentRequest.Response> {
    private static final String URL = Config.REQUEST_BASE_URL + "confirmPayment";

    private final String mSession;
    private final String mPaymentMethodId;

    public GetConfirmPaymentRequest(Context context, NetCommandListener listener, String session, int paymentMethodId) {
        super(context, listener);
        mSession = session;
        mPaymentMethodId = Integer.toString(paymentMethodId);
    }

    @Override
    protected Map<String, String> getPathParams() {
        Map<String, String> params = super.getPathParams();
        params.put("sessionId", mSession);
        params.put("paymentMethodId", mPaymentMethodId);
        return params;
    }

    @Override
    protected String getPath() {
        return URL + ParamsComposer.compose(getPathParams());
    }

    @Override
    public Response parse(String responseString) throws JSONException {
        Parser parser = new ParserImpl(UnMarshalerTypes.JSONAdapter);
        Response response = parser.parse(Response.class, responseString);
        return response;
    }

    public static class Response implements ErrorContainer {
        @XmlElement(name = "resource")
        public Sms mSms;

        @XmlElement(name="error")
        private RequestError mError;

        @Override
        public RequestError getError() {
            return mError;
        }
    }

    public static class Sms {
        @XmlElement(name = "paycode")
        String mPayCode;

        @XmlElement(name = "number")
        String mNumber;

        public String getPayCode() {
            return mPayCode;
        }

        public String getNumber() {
            return mNumber;
        }

        @Override
        public String toString() {
            return "[ mPayCode = " + mPayCode + ", mNumber = " + mNumber + " ]" ;
        }
    }
}
