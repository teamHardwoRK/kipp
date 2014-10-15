package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.BehaviorEvent;

import java.util.List;

public class BehaviorEventAdapter extends ArrayAdapter<BehaviorEvent> {
    public BehaviorEventAdapter(Context context, List<BehaviorEvent> events) {
        super(context, R.layout.item_behavior_event, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_behavior_event, parent, false);

            holder = new ViewHolder();
            holder.tvBehaviorName = (TextView) convertView.findViewById(R.id.tvBehaviorName);
            holder.tvStudentName = (TextView) convertView.findViewById(R.id.tvStudentName);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        BehaviorEvent event = getItem(position);
        holder.tvBehaviorName.setText(event.getBehavior().getTitle());
        holder.tvStudentName.setText(event.getStudent().getFirstName() + " " + event.getStudent().getLastName());

        return convertView;
    }

    class ViewHolder {
        public TextView tvBehaviorName;
        public TextView tvStudentName;
    }
}
