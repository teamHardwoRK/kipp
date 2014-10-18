package com.teamhardwork.kipp.utilities;

public class RuntimeUtils {
    public static boolean isInDebugMode() {
        return android.os.Debug.isDebuggerConnected();
    }
}
