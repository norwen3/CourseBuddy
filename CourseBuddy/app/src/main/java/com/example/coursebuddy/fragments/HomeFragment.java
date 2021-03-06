package com.example.coursebuddy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursebuddy.R;
import com.example.coursebuddy.iModels.OnItemClickListener;
import com.example.coursebuddy.adapters.AppMenuAdapter;
import com.example.coursebuddy.models.CourseModel;
import com.example.coursebuddy.models.MenuModel;
import com.example.coursebuddy.models.SemesterModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NavController navController;
    private View v;
    private RecyclerView homeMenuRv;
    private ArrayList<MenuModel> menuModelArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);


        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        menuModelArrayList = new ArrayList<>();
        menuModelArrayList.add(new CourseModel().setMenuModelItem("Courses", R.drawable.outline_pending_actions_24,
                R.id.action_homeFragment_to_coursesOverviewFragment));
        menuModelArrayList.add(new SemesterModel().setMenuModelItem("Semesters", R.drawable.outline_view_week_24,
                R.id.action_homeFragment_to_semesterOverviewFragment));

        if(view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            LinearLayoutManager lm = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(lm);
            AppMenuAdapter appMenuAdapter = new AppMenuAdapter(context, menuModelArrayList);

            recyclerView.setAdapter(appMenuAdapter);

            appMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemSelected(int position, View view, MenuModel menuModel) {
                    navController.navigate(menuModel.getNavigation());
                }

            });


        }
    }


}