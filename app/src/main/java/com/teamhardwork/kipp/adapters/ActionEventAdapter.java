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
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.utilities.DateUtilities;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class ActionEventAdapter extends ArrayAdapter<Action> implements StickyListHeadersAdapter {
    List<Action> eventList;

    private SimpleDateFormat dateFormatter;

    public ActionEventAdapter(Context context, List<Action> events) {
        super(context, R.layout.item_action_event, events);
        this.eventList = events;
        dateFormatter = new SimpleDateFormat ("EEEE");
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
        holder.tvEventTimeStamp.setText(DateUtilities.timeToString(action.getOccurredAt()));
        holder.tvEventTimeStamp.setTypeface(typeface);

        return convertView;
    }

    String age(Action event) {
        return DateUtilities.timestampAge(event.getOccurredAt()) + " " + getContext().getResources().getString(R.string.past_label);
    }

    @Override
    public View getHeaderView(int i, View convertView, ViewGroup viewGroup) {
        HeaderHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.header_behavior_event, viewGroup, false);

            holder = new HeaderHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (HeaderHolder) convertView.getTag();
        }
        Typeface typeface = KippApplication.getDefaultTypeFace(getContext());
        holder.tvHeader.setText(dateFormatter.format(getItem(i).getOccurredAt()));
        holder.tvHeader.setTypeface(typeface);

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        Action action = getItem(i);
        Date date = action.getOccurredAt();
        DateTime startOfDay = new DateTime(date).withTimeAtStartOfDay();

        return startOfDay.minusHours(7).getMillis();
    }

    class HeaderHolder {
        @InjectView(R.id.tvHeader)
        TextView tvHeader;

        public HeaderHolder(View view) {
            ButterKnife.inject(this, view);
        }
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
