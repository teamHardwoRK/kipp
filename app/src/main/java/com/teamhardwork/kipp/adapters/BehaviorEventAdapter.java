package com.teamhardwork.kipp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.utilities.DateUtilities;
import com.teamhardwork.kipp.utilities.RuntimeUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BehaviorEventAdapter extends ArrayAdapter<BehaviorEvent> {
    public BehaviorEventAdapter(Context context, List<BehaviorEvent> events) {
        super(context, R.layout.item_behavior_event, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_behavior_event, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final BehaviorEvent event = getItem(position);
        holder.tvBehaviorName.setText(event.getBehavior().getTitle());
        holder.tvStudentName.setText(event.getStudent().getFirstName() + " " + event.getStudent().getLastName());
        holder.tvEventTimestamp.setText(age(event));

        if(!RuntimeUtils.isInDebugMode()) {
            // setAgeTimer(holder.timer, holder.tvEventTimestamp, event);
        }

        return convertView;
    }

    void setAgeTimer(Timer timer, final TextView textView, final BehaviorEvent event) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final Activity activity = (Activity) getContext();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(age(event));
                    }
                });
            }
        };
        timer.schedule(task, 3000, 3000);
    }

    String age(BehaviorEvent event) {
        return DateUtilities.timestampAge(event.getOccurredAt()) +  " " + getContext().getResources().getString(R.string.past_label);
    }

    class ViewHolder {
        @InjectView(R.id.tvBehaviorName)
        TextView tvBehaviorName;

        @InjectView(R.id.tvStudentName)
        TextView tvStudentName;

        @InjectView(R.id.tvEventTimestamp)
        TextView tvEventTimestamp;

        Timer timer;
        
        public ViewHolder(View view) {
            timer = new Timer();
            ButterKnife.inject(this, view);
        }
    }
}
