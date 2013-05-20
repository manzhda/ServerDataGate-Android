package com.mda.datagate;

import java.net.URLEncoder;
import java.util.Map;

public class ParamsComposer {
//    private static final String EQUAL = URLEncoder.encode("=");
//    private static final String AND = URLEncoder.encode("&");
    private static final String EQUAL = "=";
    private static final String AND = "&";

    public static String compose(Map<String, String> pathParams) {
        if (pathParams == null || pathParams.isEmpty()) {
            return "";
        }

        StringBuilder buff = new StringBuilder();
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            String key = URLEncoder.encode(entry.getKey());
            String value = URLEncoder.encode(entry.getValue());
            buff.append(key).append(EQUAL).append(value).append(AND);
        }

        return buff.toString();
    }
}
