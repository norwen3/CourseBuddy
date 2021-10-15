package com.example.coursebuddy.models;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursebuddy.MainActivity;
import com.example.coursebuddy.R;
import com.example.coursebuddy.fragments.HomeFragment;

import java.util.ArrayList;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.Viewholder> {

    private Context context;
    private ArrayList<HomeMenuModel> homeMenuModelArrayList;

    public HomeMenuAdapter(Context context, ArrayList<HomeMenuModel> homeMenuModelArrayList){
        this.context = context;
        this.homeMenuModelArrayList = homeMenuModelArrayList;
    }

    @NonNull
    @Override
    public HomeMenuAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMenuAdapter.Viewholder holder, int position) {
        HomeMenuModel model = homeMenuModelArrayList.get(position);
        holder.menuItemName.setText(model.getMenuItemName());
        holder.menuImage.setImageResource(model.getMenuImage());
    }

    @Override
    public int getItemCount() {
        return homeMenuModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView menuImage;
        private TextView menuItemName;
        private Fragment fragment;
        private NavController navController;
        private NavHostFragment navHostFragment;
        private FragmentManager fm;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuItemName = itemView.findViewById(R.id.menuText);
            //fragment = (HomeFragment) fragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.homeFragment);
            //fm = fragment.getActivity().getSupportFragmentManager();
            //navHostFragment = (NavHostFragment) fm.findFragmentById(R.id.nav_host_fragment);
            //navController = navHostFragment.getNavController();
        }

        @Override
        public void onClick(View view) {
            //NavController navController = Navigation.findNavController(itemView);
            //navController.navigate(view.);
                int startDestination = navController.getGraph().getStartDestinationId();
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build();

                navController.navigate(startDestination, null, navOptions);
        }

    }
}
