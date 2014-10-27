package com.teamhardwork.kipp.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.BehaviorCategory;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.RandomApiUrlGenerator;

import java.util.ArrayList;
import java.util.List;

//import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by hugh_sd on 10/15/14.
 */
public class StudentArrayAdapter extends ArrayAdapter<Student> {
    private Context context;
    private ArrayList<Integer> changedPositions;

    public StudentArrayAdapter(Context context, int resource, List<Student> students) {
        super(context, resource, students);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Student student = getItem(position);
        boolean animate = false;

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

        if (changedPositions != null && changedPositions.contains(position)) {
            animate = true;
            changedPositions.remove(changedPositions.indexOf(position));
        }
        final boolean doesAnimation = animate;
        final TextView tvBehaviors = v.tvPoints;
        FeedQueries.getStudentFeed(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                if (behaviorEvents == null) return;

                StringBuilder newBehaviors = new StringBuilder();
                for (int i = 0; i < 5 && i < behaviorEvents.size(); i++) {
                    if (behaviorEvents.get(i).getBehavior().getCategory() == BehaviorCategory.SLIP ||
                            behaviorEvents.get(i).getBehavior().getCategory() == BehaviorCategory.FALL) {
                        newBehaviors.append("<font color=\"#FF6B6B\"> - </font>");
                    } else {
                        newBehaviors.append("<font color=\"#C7F464\"> + </font>");
                    }
                }

                tvBehaviors.setText(Html.fromHtml(newBehaviors.toString()));

                if (doesAnimation == true) {
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(tvBehaviors, "alpha", 0f, 1f);
                    alpha.setDuration(1000);
                    alpha.setRepeatCount(3);
                    alpha.setStartDelay(3000);
                    alpha.start();
                }
            }
        });

        return convertView;
    }

    public void updatePositions(ArrayList<Integer> positions) {
        changedPositions = new ArrayList<Integer>(positions);
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
