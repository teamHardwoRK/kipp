package com.teamhardwork.kipp.receivers;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;

public class KippPushBroadcastReceiver extends ParsePushBroadcastReceiver {
    Callback callback;

    public KippPushBroadcastReceiver(Callback callback) {
        this.callback = callback;
    }

    public KippPushBroadcastReceiver() {
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        if (callback != null) {
            callback.onPushReceive();
        }
    }

    public interface Callback {
        public void onPushReceive();
    }
}
