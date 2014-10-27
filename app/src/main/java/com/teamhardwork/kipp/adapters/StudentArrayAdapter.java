package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.squareup.picasso.Picasso;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.utilities.RandomApiUrlGenerator;

import java.util.List;

//import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hugh_sd on 10/15/14.
 */
public class StudentArrayAdapter extends ArrayAdapter<Student> {
    private Context context;

    public StudentArrayAdapter(Context context, int resource, List<Student> students) {
        super(context, resource, students);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Student student = getItem(position);

        ViewHolder v;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_student_row, parent, false);

            v.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            v.tvName = (TextView) convertView.findViewById(R.id.tvName);
            v.tvPoints = (TextView) convertView.findViewById(R.id.tvPoints);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView) parent).recycle(convertView, position);

        int imageSizePx = context.getResources().getDimensionPixelSize(R.dimen.size_list_image);
        Picasso.with(getContext())
                .load(RandomApiUrlGenerator.getUrl())
                .placeholder(R.drawable.ic_profile_placeholder)
                .resize(imageSizePx, imageSizePx)
                .into(v.ivProfilePic);

        v.tvName.setText(student.getFullName());
        v.tvPoints.setText(Integer.toString(student.getPoints()) + " points");

        return convertView;
    }

    public interface StudentAdapterListener {
        void addAction(Student student);
    }

    private static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvName;
        TextView tvPoints;
    }
}
