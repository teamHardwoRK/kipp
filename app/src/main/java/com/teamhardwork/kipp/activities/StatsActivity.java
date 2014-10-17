package com.teamhardwork.kipp.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.teamhardwork.kipp.R;

public class StatsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // TODO: Holograph graphs can't have labels on donut charts. Find a better library
        PieGraph pg = (PieGraph)findViewById(R.id.pgTest);
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#33CC00"));
        slice.setValue(2);
        slice.setTitle("Good");
        pg.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FF3333"));
        slice.setValue(3);
        slice.setTitle("Bad");
        pg.addSlice(slice);

        pg.setInnerCircleRatio(200);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
