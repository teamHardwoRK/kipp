package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.utilities.DateUtilities;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActionEventAdapter extends ArrayAdapter<Action>{
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

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_action_event, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Action action = getItem(position);

        holder.tvStudentName.setText(action.getStudent().getFullName());
        holder.tvEventTimeStamp.setText(age(action));
        holder.tvActionTypeName.setText(action.getType().getDisplayName());

        return convertView;
    }

    String age(Action event) {
        return DateUtilities.timestampAge(event.getOccurredAt()) + " " + getContext().getResources().getString(R.string.past_label);
    }

    class ViewHolder {
        @InjectView(R.id.tvActionTypeName)
        TextView tvActionTypeName;

        @InjectView(R.id.tvEventTimestamp)
        TextView tvEventTimeStamp;

        @InjectView(R.id.tvStudentName)
        TextView tvStudentName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
