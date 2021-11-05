package com.example.coursebuddy.models;

import com.example.coursebuddy.iModels.IMenuModel;
import com.google.firebase.firestore.Exclude;

public class CourseModel extends MenuModel {
    @Exclude
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
