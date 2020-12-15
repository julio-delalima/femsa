package com.femsa.sferea.Utilities;

import android.util.Log;

public class LogManager {

    private static final boolean SHOW_LOG = false;

    public static void v(String tag, String message)
    {
        if(SHOW_LOG)
            {
                Log.v(tag, message);
            }
    }

    public static void d(String tag, String message)
    {
        if(SHOW_LOG)
        {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message)
    {
        if(SHOW_LOG)
        {
            Log.e(tag, message);
        }
    }

    public static void w(String tag, String message)
    {
        if(SHOW_LOG)
        {
            Log.w(tag, message);
        }
    }

    public static void i(String tag, String message)
    {
        if(SHOW_LOG)
        {
            Log.i(tag, message);
        }
    }
}
