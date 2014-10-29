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

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ActionEventAdapter extends ArrayAdapter<Action> {
    List<Action> eventList;

    /**
     * Type of ListView item that requires to display the header section
     */
    private static final int TYPE_HEADER_ITEM = 0;

    /**
     * Type of the ListView item that does not display the header section
     */
    private static final int TYPE_REGULAR_ITEM = 1;

    private SimpleDateFormat dateFormatter;

    public ActionEventAdapter(Context context, List<Action> events) {
        super(context, R.layout.item_action_event, events);
        this.eventList = events;
        dateFormatter = new SimpleDateFormat ("EEEE");
    }

    public List<Action> getEventList() {
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

        Action previousAction = getItem(position -1);
        Action currentAction = getItem(position);

        String previousDate = dateFormatter.format(previousAction.getOccurredAt());
        String currentDate = dateFormatter.format(currentAction.getOccurredAt());

        if (currentDate.equals(previousDate)) {
            return TYPE_REGULAR_ITEM;
        } else {
            return TYPE_HEADER_ITEM;
        }
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

        int viewType = getItemViewType(position);

        if (viewType == TYPE_HEADER_ITEM) {
            holder.tvHeader.setText(dateFormatter.format(action.getOccurredAt()));
            holder.tvHeader.setVisibility(View.VISIBLE);
        } else {
            holder.tvHeader.setVisibility(View.GONE);
        }

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
        @InjectView(R.id.tvHeader)
        TextView tvHeader;
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
