package com.teamhardwork.kipp.models;

import com.teamhardwork.kipp.models.users.Student;
import com.teamhardwork.kipp.models.users.Teacher;

import java.util.Date;
import java.util.List;

public class SchoolClass {
    String name;
    Teacher teacher;
    List<Student> students;

    Date startTime;
    Date endTime;
}
