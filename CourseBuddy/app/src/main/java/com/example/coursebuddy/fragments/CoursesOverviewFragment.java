package com.example.coursebuddy.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursebuddy.MainActivity;
import com.example.coursebuddy.R;
import com.example.coursebuddy.adapters.AppMenuAdapter;
import com.example.coursebuddy.iModels.OnItemClickListener;
import com.example.coursebuddy.models.CourseModel;
import com.example.coursebuddy.models.MenuModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesOverviewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NavController navController;
    private ArrayList<MenuModel> courseList;
    private ArrayList<String> courseUidList;
    private MainActivity mainActivity;
    private AppMenuAdapter appMenuAdapter;
    private ListenerRegistration fireStoreListenerRegistration;
    private CollectionReference courseCollectionReference;
    private FirebaseFirestore firestoreDb;

    public CoursesOverviewFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesOverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesOverviewFragment newInstance(String param1, String param2) {
        CoursesOverviewFragment fragment = new CoursesOverviewFragment();
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
        return inflater.inflate(R.layout.fragment_courses_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestoreDb = FirebaseFirestore.getInstance();
        courseCollectionReference = firestoreDb.collection("courses");
        mainActivity = (MainActivity) getActivity();
        navController = Navigation.findNavController(view);
        courseUidList = new ArrayList<>();
        courseList = new ArrayList<>();

        //courseList.addAll(mainActivity.courseList);


        setThisRecyclerView(view);
        generateTestData();
    }

    private void setThisRecyclerView(@NonNull View view) {
        int navigation = R.id.action_coursesOverviewFragment_to_courseFragment;
        if(view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            LinearLayoutManager lm = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(lm);
            appMenuAdapter = new AppMenuAdapter(context, courseList);

            recyclerView.setAdapter(appMenuAdapter);

            appMenuAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemSelected(int position, View view, MenuModel menuModel) {
                    NavDirections navDirections = new NavDirections() {
                        @Override
                        public int getActionId() {
                            return navigation;
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

    private void createFirestoreReadListener() {
        fireStoreListenerRegistration = courseCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.w("FIRESTORELISTENER", "Listen failed.", error);
                    return;
                }
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    QueryDocumentSnapshot documentSnapshot = documentChange.getDocument();
                    CourseModel courseModel = documentSnapshot.toObject(CourseModel.class);
                    courseModel.setUid(documentSnapshot.getId());
                    int pos = courseUidList.indexOf(courseModel.getUid());
                    //CoursesOverviewFragment cf = (CoursesOverviewFragment) fm.findFragmentById(R.id.coursesOverviewFragment);
                    switch (documentChange.getType()){

                        case ADDED:
                            courseList.add(courseModel);
                            courseUidList.add(courseModel.getUid());
                            //cf.adapterNotifyItem("ADDED", courseList.size()-1);
                            break;
                        case REMOVED:
                            courseList.remove(pos);
                            courseUidList.remove(pos);
                            //cf.adapterNotifyItem("REMOVED",pos);
                            break;
                        case MODIFIED:
                            courseList.set(pos,courseModel);
                            //cf.adapterNotifyItem("MODIFIED",pos);
                            break;
                    }
                }
            }
        });
    }

    private void generateTestData(){
        if(courseList != null ){
            ArrayList<CourseModel> courses = new ArrayList<>();
            int img = R.drawable.sample_image;
            int nav = R.id.action_coursesOverviewFragment_to_courseFragment;
            courses.add((CourseModel) new CourseModel().setMenuModelItem("IT101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("BIO101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("IR101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("PR101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("LE101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("UT101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("SF101",img,nav));
            courses.add((CourseModel) new CourseModel().setMenuModelItem("KK101",img,nav));

            for(CourseModel course : courses){
                courseCollectionReference.add(course);
            }
        }
    }
    /*
    public void adapterNotifyItem(String reason, int pos){
        if(reason == "ADDED")
            this.appMenuAdapter.notifyItemInserted(pos);
        else if (reason == "REMOVED")
            this.appMenuAdapter.notifyItemRemoved(pos);
        else if(reason == "MODIFIED")
            this.appMenuAdapter.notifyItemChanged(pos);
    }

     */

    @Override
    public void onResume() {
        super.onResume();
        createFirestoreReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fireStoreListenerRegistration != null)
            fireStoreListenerRegistration.remove();
    }
}