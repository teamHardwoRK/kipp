package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.utilities.DateUtilities;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActionEventAdapter extends ArrayAdapter<Action> {
    List<Action> eventList;

    public ActionEventAdapter(Context context, List<Action> events) {
        super(context, R.layout.item_action_event, events);
        this.eventList = events;
    }

    public List<Action> getEventList() {
        return eventList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_action_event, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Action action = getItem(position);

        Typeface typeface = KippApplication.getDefaultTypeFace(getContext());
        holder.ivActionIcon.setImageResource(action.getType().getGreenResource());
        holder.tvActionTypeName.setTypeface(typeface);
        holder.tvActionTypeName.setText(action.getType().getDisplayName());
        holder.tvEventTimeStamp.setText(age(action));
        holder.tvEventTimeStamp.setTypeface(typeface);

        return convertView;
    }

    String age(Action event) {
        return DateUtilities.timestampAge(event.getOccurredAt()) + " " + getContext().getResources().getString(R.string.past_label);
    }

    class ViewHolder {
        @InjectView(R.id.ivActionIcon)
        ImageView ivActionIcon;
        @InjectView(R.id.tvActionTypeName)
        TextView tvActionTypeName;
        @InjectView(R.id.tvEventTimestamp)
        TextView tvEventTimeStamp;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
