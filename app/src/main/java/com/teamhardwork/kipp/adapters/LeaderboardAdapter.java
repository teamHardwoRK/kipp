package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.users.Student;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardAdapter extends ArrayAdapter<Student> {

    public LeaderboardAdapter(Context context, List<Student> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_leaderboard_entry, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        Student student = getItem(position);
        populateViewHolder(holder, student, position + 1);

        return convertView;
    }

    private void populateViewHolder(ViewHolder holder, Student student, int position) {
        holder.tvRank.setText(Integer.toString(position));
        holder.tvName.setText(student.getFirstName() + " " + student.getLastName());
        holder.tvPoints.setText(Integer.toString(student.getPoints()) + " points");
    }

    static class ViewHolder {
        @InjectView(R.id.tvRank)
        TextView tvRank;

        @InjectView(R.id.tvName)
        TextView tvName;

        @InjectView(R.id.tvPoints)
        TextView tvPoints;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
