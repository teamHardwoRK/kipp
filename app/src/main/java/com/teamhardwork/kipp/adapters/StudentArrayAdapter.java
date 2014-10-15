package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.users.Student;

import java.util.List;

//import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hugh_sd on 10/15/14.
 */
public class StudentArrayAdapter extends ArrayAdapter<Student> {
    private static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvName;
        TextView tvDescription;
    }

    public StudentArrayAdapter(Context context, int resource, List<Student> students) {
        super(context, resource, students);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);

        ViewHolder v;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.student_row, parent, false);
            v.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            v.tvName = (TextView) convertView.findViewById(R.id.tvName);
            v.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

//        v.ivProfilePic.setImageResource(android.R.color.transparent);
        v.ivProfilePic.setImageResource(R.drawable.ic_logo);
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(student.getProfilePicUrl(),
//                v.ivProfilePic);
        v.tvName.setText(Html.fromHtml("<b>" + student.getFirstName() + "</b>"));
        v.tvDescription.setText(Html.fromHtml("<i>" + student.getGender().toString() + "</i>"));

        return convertView;
    }
}
