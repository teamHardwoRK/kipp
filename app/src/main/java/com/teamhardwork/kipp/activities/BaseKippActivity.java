package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.receivers.KippPushBroadcastReceiver;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by rcarino on 10/26/14.
 */
public abstract class BaseKippActivity extends Activity {
    private KippPushBroadcastReceiver pushReceiver;

    @Override
    public void onBackPressed() {
        exitActivity();
    }

    protected void exitActivity() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    protected void enterActivity(Intent i) {
        startActivity(i);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pushReceiver == null) {
            registerPushReceiver();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pushReceiver != null) {
            try {
                this.unregisterReceiver(pushReceiver);
                pushReceiver = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void updateFragments() {
        Crouton.makeText(this, "Data Updated", Style.INFO).show();
    }

    private void registerPushReceiver() {
        pushReceiver = new KippPushBroadcastReceiver(new KippPushBroadcastReceiver.Callback() {
            @Override
            public void onPushReceive() {
                updateFragments();
            }
        });

        IntentFilter receiveIntentFilter = new IntentFilter();
        receiveIntentFilter.addAction("com.parse.push.intent.RECEIVE");
        receiveIntentFilter.addAction("com.parse.push.intent.DELETE");
        receiveIntentFilter.addAction("com.parse.push.intent.OPEN");

        this.registerReceiver(pushReceiver, receiveIntentFilter);
    }
}
