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

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class BehaviorEventAdapter extends ArrayAdapter<BehaviorEvent> implements StickyListHeadersAdapter {
    List<BehaviorEvent> eventList;
    boolean classMode;

    private SimpleDateFormat dateFormatter;

    public BehaviorEventAdapter(Context context, List<BehaviorEvent> eventList, boolean isClassMode) {
        super(context, 0, eventList);
        this.eventList = eventList;
        classMode = isClassMode;
        dateFormatter = new SimpleDateFormat ("EEEE");
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

        // TODO: Make use of new method in DateUtilities to print time 'HH:MM a'
        holder.tvEventTimestamp.setText(age(event));
        holder.tvEventTimestamp.setTypeface(typeface);
        holder.ivBehaviorIcon.setImageResource(event.getBehavior().getColorResource());

        return convertView;
    }

    String age(BehaviorEvent event) {
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
        BehaviorEvent event = getItem(i);
        Date date = event.getOccurredAt();
        DateTime startOfDay = new DateTime(date).withTimeAtStartOfDay();

        return startOfDay.getMillis();
    }

    class HeaderHolder {
        @InjectView(R.id.tvHeader)
        TextView tvHeader;

        public HeaderHolder(View view) {
            ButterKnife.inject(this, view);
        }
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
