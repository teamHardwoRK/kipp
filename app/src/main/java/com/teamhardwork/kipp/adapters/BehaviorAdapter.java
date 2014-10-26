package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;

import java.util.List;

public class BehaviorAdapter extends ArrayAdapter<Behavior>{

    public BehaviorAdapter(Context context, List<Behavior> objects) {
        super(context, R.layout.item_behavior, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_behavior, parent, false);

            holder = new ViewHolder();
            holder.tvBehaviorName = (TextView) convertView.findViewById(R.id.tvBehaviorName);
            holder.ivBehaviorIcon = (ImageView) convertView.findViewById(R.id.ivBehaviorIcon);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Behavior behavior = getItem(position);

        holder.ivBehaviorIcon.setImageResource(behavior.getResource());
        holder.tvBehaviorName.setText(behavior.getTitle());

        return convertView;
    }

    class ViewHolder {
        public TextView tvBehaviorName;
        public ImageView ivBehaviorIcon;
    }
}
