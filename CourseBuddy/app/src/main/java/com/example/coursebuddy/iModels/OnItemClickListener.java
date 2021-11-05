package com.example.coursebuddy.iModels;

import android.view.View;

import com.example.coursebuddy.models.MenuModel;

public interface OnItemClickListener {
    void onItemSelected(int position, View view, MenuModel menuModel);
}
