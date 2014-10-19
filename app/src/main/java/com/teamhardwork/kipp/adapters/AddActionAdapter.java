package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.ActionType;
import com.teamhardwork.kipp.enums.Role;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddActionAdapter extends ArrayAdapter<ActionType> {

    public AddActionAdapter(Context context, Role role) {
        super(context, R.layout.item_add_action, ActionType.findForRole(role));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_add_action, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ActionType actionType = getItem(position);

        holder.tvActionTypeName.setText(actionType.getDisplayName());

        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.tvActionTypeName)
        TextView tvActionTypeName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
