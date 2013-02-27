package com.mda.datagate.utils;

import android.util.Log;

/**
 * Helper class to log
 *
 * @author "Pavel Martovskiy"
 */
public class MyLog {
    private static final String TAG = "SERVERDATAGATE";

    public static void v(Object... params) {
        vt(TAG, params);
    }

    public static void vt(String tag, Object... params) {
        StringBuilder message = new StringBuilder();

        for (Object object : params) {
            message.append(object == null ? "null" : object.toString());
            message.append(" ");
        }
        Log.v(tag, message.toString());
    }
}
