package com.example.coursebuddy.models;

import com.example.coursebuddy.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SemesterModel extends MenuModel{

    private Date startDate, endDate;
    private List<CourseModel> courseModelList = new ArrayList<>();
    private Boolean active = true;

}
