package com.mda.datagate;

import android.os.AsyncTask;

import java.util.HashMap;

/**
 * User: MAX
 * Date: 6/7/13
 * Time: 3:43 PM
 */
public class RequestExecuter {

    public static void executeRequest(AbstractRequest request){
             new RequestAsyncTask().execute(request);
    }

    private static class RequestAsyncTask extends AsyncTask<AbstractRequest, Void, RequestResponseContainer> {
        @Override
        protected RequestResponseContainer doInBackground(AbstractRequest... requests) {
            if (!DataGate.isInternetPresent(requests[0].getContext())) {
                return new RequestResponseContainer(requests[0],
                        new Response(com.mda.datagate.Status.NO_INTERNET_CONNECTION));
            }
            if(!requests[0].needToRetry()){
                  return Controller.execute(requests[0]);
             }
            else{
                  return getRequestResponseContainer(requests[0]);
            }
        }

        @Override
        protected void onPostExecute(RequestResponseContainer result) {
            AbstractRequest request = result.getRequest();
            Response response = result.getResponse();
            request.isNeedReturnHeader();
            com.mda.datagate.Status responseCode = response.getCode();

            NetCommandListener listener = request.getListener();
            if (listener != null) {
                callListener(result, request, response, responseCode, listener);
            }
        }

        private void callListener(RequestResponseContainer result, AbstractRequest request, Response response, com.mda.datagate.Status responseCode, NetCommandListener listener) {
            if (responseCode == com.mda.datagate.Status.OK||responseCode == com.mda.datagate.Status.ACCEPTED) {
                if(request.isNeedReturnHeader()){
                    if(result.getHeadersList()!=null){
                        ResponseWithHeaders rwh = new ResponseWithHeaders(response.getData(),result.getHeadersList());
                        listener.onComplete(request, rwh);
                    }
                }
                else {
                    listener.onComplete(request, response.getData());
                }
            } else {
                listener.onError(responseCode, response.getData());
            }
        }

        private RequestResponseContainer getRequestResponseContainer(AbstractRequest request) {
            int count = 0;
            int retry = 10;

            do{
                if(request.isAborted()) break;
                RequestResponseContainer result = Controller.execute(request);
                count++;
                if(com.mda.datagate.Status.OK == result.getResponse().getCode()){
                    return result;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    return new RequestResponseContainer(request, new Response(com.mda.datagate.Status.DATA_UNAVAILABLE));
                }
            }
            while (count<=retry);

            return new RequestResponseContainer(request, new Response(com.mda.datagate.Status.DATA_UNAVAILABLE));
        }
    }

    public static class ResponseWithHeaders{

        Object mData;
        HashMap<String, String> mHeadersList;

        public ResponseWithHeaders(Object data, HashMap<String, String> headersList) {
             mData = data;
             mHeadersList = headersList;
        }

        public Object getData() {
            return mData;
        }

        public void setData(Object data) {
            mData = data;
        }

        public HashMap<String, String> getHeadersList() {
            return mHeadersList;
        }

        public void setHeadersList(HashMap<String, String> headersList) {
            mHeadersList = headersList;
        }
    }
}
