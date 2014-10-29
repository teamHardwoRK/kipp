package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.enums.Behavior;
import com.teamhardwork.kipp.enums.BehaviorCategory;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.queries.FeedQueries;
import com.teamhardwork.kipp.utilities.Recommendation;

import java.util.List;

public class StudentArrayAdapter extends ArrayAdapter<Student> {
    private Context context;
    public static final int warningColor = Color.parseColor("#FFD119"); // color for warning tips
    public static final int infoColor = Color.parseColor("#1C70EF");

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
            v.tvRecentBehaviors = (TextView) convertView.findViewById(R.id.tvRecentBehaviors);
            v.tvNewBehavior = (TextView) convertView.findViewById(R.id.tvNewBehavior);
            v.ivTips = (ImageView) convertView.findViewById(R.id.ivTips);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView) parent).recycle(convertView, position);

        Typeface typeface = KippApplication.getDefaultTypeFace(getContext());
        v.ivProfilePic.setImageResource(student.getAvatar().getResourceId());
        v.tvName.setTypeface(typeface);
        v.tvName.setText(student.getFullName());
        v.tvRecentBehaviors.setText("");
        v.tvNewBehavior.setText("");
        updateStudentInfo(student, v.tvRecentBehaviors, v.tvNewBehavior, v.ivTips);

        return convertView;
    }

    public void updateStudentInfo(final Student student,
                                  final TextView tvRecentBehaviors,
                                  final TextView tvNewBehavior,
                                  final ImageView ivTips) {
        ivTips.setImageResource(0);
        FeedQueries.getStudentFeedMostRecent(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                if (behaviorEvents == null || behaviorEvents.isEmpty()) return;

                if (Recommendation.getInstance().hasRecs(student)) {
                    ivTips.setImageResource(R.drawable.ic_tips);
                    int tipColor = (Recommendation.getInstance().getRecs(student).getRecType() == Recommendation.RecommendationType.BAD) ?
                            warningColor : infoColor;
                    ivTips.setColorFilter(tipColor);
                }

                StringBuilder recentBehaviors = new StringBuilder();
                int behaviorsSize = Math.min(FeedQueries.MOST_RECENT_MAX, behaviorEvents.size());

                for (int i = behaviorsSize - 1; i >= 1; i--) {
                    recentBehaviors.append(getBehaviorHtmlString(behaviorEvents.get(i).getBehavior()));
                }

                tvRecentBehaviors.setText(Html.fromHtml(recentBehaviors.toString()));
                tvNewBehavior.setText(Html.fromHtml(getBehaviorHtmlString(behaviorEvents.get(0).getBehavior())));
            }
        });
    }

    public static String getBehaviorHtmlString(Behavior behavior) {
        if (behavior.getCategory() == BehaviorCategory.SLIP ||
                behavior.getCategory() == BehaviorCategory.FALL) {
            return new String("<font color=\"#FF6B6B\"> - </font>");
        } else {
            return new String("<font color=\"#C7F464\"> + </font>");
        }
    }

    private static class ViewHolder {
        ImageView ivProfilePic;
        TextView tvName;
        TextView tvRecentBehaviors;
        TextView tvNewBehavior;
        ImageView ivTips;
    }
}
