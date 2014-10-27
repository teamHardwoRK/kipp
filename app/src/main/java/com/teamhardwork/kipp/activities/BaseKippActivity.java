package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.content.Intent;

import com.teamhardwork.kipp.R;

/**
 * Created by rcarino on 10/26/14.
 */
public abstract class BaseKippActivity extends Activity {
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
}
