package com.liberty;

/**
 * Created by liupeng on 2017/11/30.
 */


import android.util.Log;

public class Logger {

    public static boolean DEBUG = false;

    public static void debug(boolean debug) {
        DEBUG = debug;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void d(String tag, String msg) {
        if (!DEBUG) {
            return;
        }
        Log.d(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (!DEBUG) {
            return;
        }
        Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (!DEBUG) {
            return;
        }
        Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (!DEBUG) {
            return;
        }
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        if (!DEBUG) {
            return;
        }
        Log.e(tag, msg, e);
    }
}
