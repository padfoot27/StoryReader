package com.example.onlinetyari.storyreader.debug;

import android.util.Log;

import com.example.onlinetyari.storyreader.constants.DebugConstants;

/**
 * Created by Siddharth Verma on 10/6/16.
 */


public class DebugHandler {

    public static void Log(String logType, String log) {

        assert logType != null;
        assert log != null;

        if (DebugConstants.ENABLE_LOGGING)
            Log.i(logType, log);
    }

    public static void LogException(String logType, Exception exception) {

        assert logType != null;
        assert exception != null;

        if (DebugConstants.ENABLE_LOGGING)
            Log.e(logType, DebugConstants.EXCEPTION + exception);
    }
}

