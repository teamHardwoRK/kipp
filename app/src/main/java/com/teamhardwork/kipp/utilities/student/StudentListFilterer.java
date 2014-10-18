package com.teamhardwork.kipp.utilities.student;

import com.teamhardwork.kipp.models.users.Student;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentListFilterer {

    private static Comparator<Student> pointsComparator = new Comparator<Student>() {
        @Override
        public int compare(Student student, Student student2) {
            return Integer.valueOf(student.getPoints()).compareTo(student2.getPoints());
        }
    };

    public static List<Student> keepTopNDescending(List<Student> input, int n) {
        Collections.sort(input, pointsComparator);
        n = Math.min(n, input.size());

        List<Student> topNDescending = input.subList(0, n); // not true yet
        Collections.reverse(topNDescending); // now it's descending

        return topNDescending;
    }
}
