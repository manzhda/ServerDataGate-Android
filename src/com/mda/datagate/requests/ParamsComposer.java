package com.mda.datagate.requests;

import java.net.URLEncoder;
import java.util.Map;

public class ParamsComposer {
    private static final String BRACE_LEFT = URLEncoder.encode("{");
    private static final String BRACE_RIGHT = URLEncoder.encode("}");
    private static final String QUOTE = URLEncoder.encode("\"");

    public static String compose(Map<String, String> pathParams) {
        if (pathParams == null || pathParams.isEmpty()) {
            return "";
        }

        StringBuilder buff = new StringBuilder();
        buff.append("/?request=").append(BRACE_LEFT);
        boolean removeComma = false;
        for (Map.Entry<String, String> entry : pathParams.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            removeComma = true;
            String key = URLEncoder.encode(entry.getKey());
            String value = URLEncoder.encode(entry.getValue());
            buff.append(QUOTE).append(key).append(QUOTE).append(":").append(QUOTE).append(value).append(QUOTE).append(",");
        }

        if (removeComma) {
            int len = buff.length();
            buff.delete(len - 1, len);
        }

        buff.append(BRACE_RIGHT);

        return buff.toString();
    }
}
