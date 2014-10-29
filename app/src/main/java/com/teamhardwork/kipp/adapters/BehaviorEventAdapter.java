package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.utilities.DateUtilities;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BehaviorEventAdapter extends ArrayAdapter<BehaviorEvent> {
    List<BehaviorEvent> eventList;
    boolean classMode;

    public BehaviorEventAdapter(Context context, List<BehaviorEvent> eventList, boolean isClassMode) {
        super(context, 0, eventList);
        this.eventList = eventList;
        classMode = isClassMode;
    }

    public List<BehaviorEvent> getEventList() {
        return eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            if (classMode) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_behavior_event, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_behavior_event_student, parent, false);
            }
            holder = new ViewHolder(convertView);

            if(classMode) {
                holder.tvStudentName = (TextView) convertView.findViewById(R.id.tvStudentName);
            }
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface typeface = KippApplication.getDefaultTypeFace(getContext());
        BehaviorEvent event = getItem(position);
        holder.tvBehaviorName.setText(event.getBehavior().getTitle());
        holder.tvBehaviorName.setTypeface(typeface);

        if(classMode) {
            holder.tvStudentName.setText(event.getStudent().getFirstName() + " " + event.getStudent().getLastName());
            holder.tvStudentName.setTypeface(typeface);
        }

        holder.tvEventTimestamp.setText(age(event));
        holder.tvEventTimestamp.setTypeface(typeface);
        holder.ivBehaviorIcon.setImageResource(event.getBehavior().getColorResource());

        return convertView;
    }

    String age(BehaviorEvent event) {
        return DateUtilities.timestampAge(event.getOccurredAt()) + " " + getContext().getResources().getString(R.string.past_label);
    }

    @Override
    public int getItemViewType(int position) {
        if(classMode) {
            return 0;
        }
        else {
            return 1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ViewHolder {
        @InjectView(R.id.tvBehaviorName)
        TextView tvBehaviorName;
        @InjectView(R.id.tvEventTimestamp)
        TextView tvEventTimestamp;
        @InjectView(R.id.ivBehaviorIcon)
        ImageView ivBehaviorIcon;

        public TextView tvStudentName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
