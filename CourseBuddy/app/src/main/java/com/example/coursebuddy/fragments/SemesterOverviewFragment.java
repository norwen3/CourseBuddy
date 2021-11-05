package com.example.coursebuddy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursebuddy.R;
import com.example.coursebuddy.adapters.AppMenuAdapter;
import com.example.coursebuddy.iModels.OnItemClickListener;
import com.example.coursebuddy.models.CourseModel;
import com.example.coursebuddy.models.MenuModel;
import com.example.coursebuddy.models.SemesterModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SemesterOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SemesterOverviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NavController navController;
    private ArrayList<MenuModel> semesterArrayList;
    FloatingActionButton fab;

    public SemesterOverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SemesterOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SemesterOverviewFragment newInstance(String param1, String param2) {
        SemesterOverviewFragment fragment = new SemesterOverviewFragment();
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
    public void onDetach() {
        fab.hide();
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        fab.hide();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_semester_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        int navigation = R.id.action_semesterOverviewFragment_to_semesterFragment;
        semesterArrayList = new ArrayList<>();
        semesterArrayList.add(new SemesterModel().setMenuModelItem("Semester 1", R.drawable.outline_pending_actions_24,
                navigation));
        semesterArrayList.add(new SemesterModel().setMenuModelItem("Semester 2", R.drawable.outline_pending_actions_24,
                navigation));
        fab = this.getActivity().findViewById(R.id.fab);
        fab.show();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_semesterOverviewFragment_to_newSemesterFragment);
            }
        });


        if(view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            LinearLayoutManager lm = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(lm);
            AppMenuAdapter appMenuAdapter = new AppMenuAdapter(context, semesterArrayList);

            recyclerView.setAdapter(appMenuAdapter);

            appMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemSelected(int position, View view, MenuModel menuModel) {
                    NavDirections navDirections = new NavDirections() {
                        @Override
                        public int getActionId() {
                            return R.id.action_semesterOverviewFragment_to_semesterFragment;
                        }

                        @NonNull
                        @Override
                        public Bundle getArguments() {
                            Bundle args = new Bundle();
                            args.putString("title", menuModel.getMenuItemName());
                            return args;
                        }
                    };
                    navController.navigate(navDirections);
                }

            });
        }
    }
}