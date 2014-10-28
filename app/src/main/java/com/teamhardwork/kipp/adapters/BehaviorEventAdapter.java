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
        super(context, R.layout.item_behavior_event, eventList);
        this.eventList = eventList;
        classMode = isClassMode;
    }

    public List<BehaviorEvent> getEventList() {
        return eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_behavior_event, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface typeface = KippApplication.getDefaultTypeFace(getContext());
        final BehaviorEvent event = getItem(position);
        holder.tvBehaviorName.setText(event.getBehavior().getTitle());
        holder.tvBehaviorName.setTypeface(typeface);
        holder.tvStudentName.setText(event.getStudent().getFirstName() + " " + event.getStudent().getLastName());
        holder.tvStudentName.setTypeface(typeface);
        if (!classMode) {
            holder.tvStudentName.setVisibility(View.GONE);
        }
        holder.tvEventTimestamp.setText(age(event));
        holder.tvEventTimestamp.setTypeface(typeface);
        holder.ivBehaviorIcon.setImageResource(event.getBehavior().getColorResource());

        return convertView;
    }

    String age(BehaviorEvent event) {
        return DateUtilities.timestampAge(event.getOccurredAt()) + " " + getContext().getResources().getString(R.string.past_label);
    }

    class ViewHolder {
        @InjectView(R.id.tvBehaviorName)
        TextView tvBehaviorName;
        @InjectView(R.id.tvStudentName)
        TextView tvStudentName;
        @InjectView(R.id.tvEventTimestamp)
        TextView tvEventTimestamp;
        @InjectView(R.id.ivBehaviorIcon)
        ImageView ivBehaviorIcon;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
