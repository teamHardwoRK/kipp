package com.teamhardwork.kipp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StudentArrayAdapter extends ArrayAdapter<Student> implements Filterable {
    public static final int warningColor = Color.parseColor("#FFD119"); // color for warning tips
    public static final int infoColor = Color.parseColor("#1C70EF");
    private Context context;
    private List<Student> students;
    private List<Student> originalRoster;
    private Filter studentFilter;

    public StudentArrayAdapter(Context context, int resource, List<Student> students) {
        super(context, resource, students);
        this.context = context;
        this.students = students;
        this.originalRoster = new ArrayList<Student>(students);
    }

    public static String getBehaviorHtmlString(Behavior behavior) {
        if (behavior.getCategory() == BehaviorCategory.SLIP ||
                behavior.getCategory() == BehaviorCategory.FALL) {
            return new String("<font color=\"#FF6B6B\"> - </font>");
        } else {
            return new String("<font color=\"#C7F464\"> + </font>");
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Student student = getItem(position);

        ViewHolder v;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_student_row, parent, false);

            v = new ViewHolder(convertView);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView) parent).recycle(convertView, position);

        Typeface typeface = KippApplication.getDefaultTypeFace(getContext());
        v.ivProfilePic.setImageResource(student.getAvatar().getResourceId());
        v.tvName.setTypeface(typeface);
        v.tvName.setText(student.getFullName());
        hideAllRecentBehaviors(v);

        updateStudentInfo(student, v);

        return convertView;
    }

    void hideAllRecentBehaviors(ViewHolder holder) {
        holder.ivFirstBehavior.setVisibility(View.GONE);
        holder.ivSecondBehavior.setVisibility(View.GONE);
        holder.ivThirdBehavior.setVisibility(View.GONE);
        holder.ivFourthBehavior.setVisibility(View.GONE);
        holder.ivLastBehavior.setVisibility(View.GONE);
    }

    public void updateStudentInfo(final Student student, final ViewHolder holder) {
        holder.ivTips.setImageResource(0);

        FeedQueries.getStudentFeedMostRecent(student, new FindCallback<BehaviorEvent>() {
            @Override
            public void done(List<BehaviorEvent> behaviorEvents, ParseException e) {
                if (behaviorEvents == null || behaviorEvents.isEmpty()) return;

                Collections.reverse(behaviorEvents);

                if (Recommendation.getInstance().hasRecs(student)) {
                    holder.ivTips.setImageResource(R.drawable.ic_tips);
                    int tipColor = (Recommendation.getInstance().getRecs(student).getRecType()
                            == Recommendation.RecommendationType.BAD) ?
                            warningColor : infoColor;
                    holder.ivTips.setColorFilter(tipColor);
                }

                for (int i = behaviorEvents.size() - 1; i >= 0; i--) {
                    int eventResource = behaviorEvents.get(i).getBehavior().getColorResource();

                    if (i == behaviorEvents.size() - 1) {
                        holder.ivLastBehavior.setImageResource(eventResource);
                        holder.ivLastBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 2) {
                        holder.ivFourthBehavior.setImageResource(eventResource);
                        holder.ivFourthBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 3) {
                        holder.ivThirdBehavior.setImageResource(eventResource);
                        holder.ivThirdBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 4) {
                        holder.ivSecondBehavior.setImageResource(eventResource);
                        holder.ivSecondBehavior.setVisibility(View.VISIBLE);
                    } else if (i == behaviorEvents.size() - 5) {
                        holder.ivFirstBehavior.setImageResource(eventResource);
                        holder.ivFirstBehavior.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void resetFilter() {
        clear();
        addAll(originalRoster);
    }

    public Filter getFilter() {
        if (studentFilter == null) {
            studentFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    List<Student> filteredStudents = new ArrayList<Student>();

                    for (Student student : originalRoster) {
                        if (student.getFirstName().toLowerCase().startsWith(constraint.toString()))
                            filteredStudents.add(student);
                    }
                    results.values = filteredStudents;
                    results.count = filteredStudents.size();
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    // Now we have to inform the adapter about the new list filtered
                    if (results.count == 0)
                        notifyDataSetInvalidated();
                    else {
                        clear();
                        addAll((List<Student>) results.values);
                    }
                }
            };
        }

        return studentFilter;
    }

    public void setOriginalRoster(List<Student> originalRoster) {
        this.originalRoster = originalRoster;
    }

    static class ViewHolder {
        @InjectView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @InjectView(R.id.tvName)
        TextView tvName;
        @InjectView(R.id.ivFirstBehavior)
        ImageView ivFirstBehavior;
        @InjectView(R.id.ivSecondBehavior)
        ImageView ivSecondBehavior;
        @InjectView(R.id.ivThirdBehavior)
        ImageView ivThirdBehavior;
        @InjectView(R.id.ivFourthBehavior)
        ImageView ivFourthBehavior;
        @InjectView(R.id.ivLastBehavior)
        ImageView ivLastBehavior;
        @InjectView(R.id.ivTips)
        ImageView ivTips;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
