package com.example.coursebuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.coursebuddy.fragments.CoursesOverviewFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private FragmentManager fm;
    private NavController navController;
    private Fragment fragment;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_drawer_view);
        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fm = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fm.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setFallbackOnNavigateUpListener(this::onSupportNavigateUp)
                        .setOpenableLayout(drawerLayout)
                        .build();
        toolbar = findViewById(R.id.topAppBar);

        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    public boolean onNavigateUp() {
        navController.navigateUp();
        return super.onNavigateUp();
    }


    /*
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.homeFragment:
                HomeFragment homeFragment = new HomeFragment();
                navigationFragmentSelected(homeFragment);
                return true;


            case R.id.favouritesFragment:
                navigationFragmentSelected(new FavouritesFragment());
                return true;
        }


        return false;
    }

    private void navigationFragmentSelected(Fragment selected){
        fm.beginTransaction().replace(R.id.nav_host_fragment, selected).commit();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeFragment:
                // User chose the "Home" item, show the app Home UI...

                navigationFragmentSelected(new HomeFragment());
                return true;

            case R.id.favouritesFragment:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                navigationFragmentSelected(new FavouritesFragment());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
*/


}