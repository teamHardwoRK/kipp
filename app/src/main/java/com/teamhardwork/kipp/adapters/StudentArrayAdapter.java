package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.models.users.Student;

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
                    R.layout.student_row, parent, false);
            v.ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
            v.tvName = (TextView) convertView.findViewById(R.id.tvName);
            v.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            v.btnNote = (Button) convertView.findViewById(R.id.btnNote);
            v.btnContact = (Button) convertView.findViewById(R.id.btnContact);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView) parent).recycle(convertView, position);

        v.ivProfilePic.setImageResource(R.drawable.ic_logo);
        v.tvName.setText(Html.fromHtml("<b>" + student.getFirstName() + " " + student.getLastName() + "</b>"));
        v.tvDescription.setText(Html.fromHtml("<i>" + student.getGender().toString() + "</i>"));

        v.btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "btnNote pressed!", Toast.LENGTH_SHORT).show();
            }
        });

        v.btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StudentAdapterListener) getContext()).addAction(student);
            }
        });
        return convertView;
    }

    public interface StudentAdapterListener {
        void addAction(Student student);
    }

    private static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvName;
        TextView tvDescription;
        Button btnNote;
        Button btnContact;
    }
}
