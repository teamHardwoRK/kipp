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

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BehaviorEventAdapter extends ArrayAdapter<BehaviorEvent> {
    List<BehaviorEvent> eventList;
    boolean classMode;

    /**
     * Type of ListView item that requires to display the header section
     */
    private static final int TYPE_HEADER_ITEM = 0;

    /**
     * Type of the ListView item that does not display the header section
     */
    private static final int TYPE_REGULAR_ITEM = 1;

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

    /**
     * Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
     */
    @Override
    public int getViewTypeCount() {
        // At the moment we have 2 types:
        // 0 - TYPE_HEADER_ITEM
        // 1 - TYPE_REGULAR_ITEM
        return 2;
    }

    /**
     * Get the type of View that will be created by getView(int, View, ViewGroup) for the
     * specified item.
     *
     * @param position
     * @return the view type
     */
    @Override
    public int getItemViewType(int position) {
        // Check BehaviorEvent date vs previous element date
        if (position == 0) {
            return TYPE_HEADER_ITEM; // First item have header
        }

        BehaviorEvent previousBehavior = getItem(position -1);
        BehaviorEvent currentBehavior = getItem(position);

        String previousDate = dateFormatter.format(previousBehavior.getOccurredAt());
        String currentDate = dateFormatter.format(currentBehavior.getOccurredAt());

        if (currentDate.equals(previousDate)) {
            return TYPE_REGULAR_ITEM;
        } else {
            return TYPE_HEADER_ITEM;
        }
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

        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER_ITEM) {
            holder.tvHeader.setText(dateFormatter.format(event.getOccurredAt()));
            holder.tvHeader.setVisibility(View.VISIBLE);
        } else {
            holder.tvHeader.setVisibility(View.GONE);
        }

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

/*    @Override
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
    }*/

    class ViewHolder {
        @InjectView(R.id.tvHeader)
        TextView tvHeader;
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
