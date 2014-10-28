package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.utilities.GraphicsUtils;
import com.teamhardwork.kipp.utilities.RandomApiUrlGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LeaderboardAdapter extends ArrayAdapter<Student> {
    List<Integer> colorList = new ArrayList<Integer>();

    public LeaderboardAdapter(Context context, List<Student> objects) {
        super(context, 0, objects);

        colorList = GraphicsUtils.colorList(context);
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
            Typeface defaultTypeface = KippApplication.getDefaultTypeFace(getContext());
            holder.tvName.setTypeface(defaultTypeface);
            holder.tvPoints.setTypeface(defaultTypeface);
            holder.tvRank.setTypeface(defaultTypeface);
            convertView.setTag(holder);
        }

        int backgroundColor = colorList.get(new Random().nextInt(colorList.size()));
        ((GradientDrawable)holder.tvRank.getBackground()).setColor(
                backgroundColor);
        Student student = getItem(position);
        populateViewHolder(holder, student, position + 1);

        return convertView;
    }

    private void populateViewHolder(ViewHolder holder, Student student, int position) {
        holder.tvRank.setText(Integer.toString(position));
        holder.tvName.setText(student.getFirstName() + " " + student.getLastName());
        holder.tvPoints.setText(Integer.toString(student.getPoints()) + " points");
        Picasso.with(getContext())
                .load(RandomApiUrlGenerator.getUrl())
                .placeholder(R.drawable.ic_profile_placeholder)
                .resize(150, 150)
                .into(holder.ivProfilePic);
    }

    static class ViewHolder {
        @InjectView(R.id.tvRank)
        TextView tvRank;
        @InjectView(R.id.tvName)
        TextView tvName;
        @InjectView(R.id.tvPoints)
        TextView tvPoints;
        @InjectView(R.id.ivProfilePic)
        ImageView ivProfilePic;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
