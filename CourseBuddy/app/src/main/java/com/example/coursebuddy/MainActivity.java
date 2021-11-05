package com.example.coursebuddy;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.coursebuddy.adapters.AppMenuAdapter;
import com.example.coursebuddy.fragments.CoursesOverviewFragment;
import com.example.coursebuddy.models.CourseModel;
import com.example.coursebuddy.models.SemesterModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    //frontend
    private BottomNavigationView bottomNav;
    private FragmentManager fm;
    private NavController navController;
    private Fragment fragment;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton fab;
    //Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    //database
    private FirebaseFirestore firestoreDb;
    private CollectionReference courseCollectionReference;
    private ListenerRegistration fireStoreListenerRegistration;
    //models
    public List<CourseModel> courseList;
    private List<String> courseUidList;


    private final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();
        createAuthStateListener();


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_drawer_view);

        bottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fm = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fm.findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        fab = findViewById(R.id.fab);
        fab.hide();



        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setFallbackOnNavigateUpListener(this::onSupportNavigateUp)
                        .setOpenableLayout(drawerLayout)
                        .build();

        toolbar = findViewById(R.id.topAppBar);

        NavigationUI.setupWithNavController(bottomNav, navController);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Button logoutButton = new Button(getApplicationContext());

        navigationView.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(menuItem -> {
            logOut();
            return true;
                });



        //generateTestData();

        //navigationView.addView(navigationView);

    }


    protected void logOut(){
        AuthUI.getInstance().signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "User NOT logged in", Toast.LENGTH_SHORT).show();

        }
    }

    private void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), user.getEmail() + " logged in",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            createSignInIntent();
                        }
                    }
                });
    }

    private void createAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build());

                    // Create and launch sign-in intent
                    //startActivityForResult(
                    //        AuthUI.getInstance()
                    //                .createSignInIntentBuilder()
                    //                .setAvailableProviders(providers)
                    //                .build(),
                    //        RC_SIGN_IN);

                    signInLauncher.launch(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false)
                            .build());

                }
                else {
                    Toast.makeText(getApplicationContext(), "Signed in as " + currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }



    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    protected void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Toast.makeText(getApplicationContext(), "Signed in as " + currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Signed in cancelled", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI(FirebaseUser user) {
    }

    private void reload() {

    }

    @Override
    public boolean onNavigateUp() {
        navController.navigateUp();
        return super.onNavigateUp();
    }
/*
    private void createFirestoreReadListener() {
        fireStoreListenerRegistration = courseCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.w(TAG, "Listen failed.", error);
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
*/


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
        //createFirestoreReadListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
        /*
        if (fireStoreListenerRegistration != null)
            fireStoreListenerRegistration.remove();

         */
    }





/*
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item == findViewById(R.id.logout)){
            logOut();
            return true;
        }


        return super.onSupportNavigateUp();
    }
/*
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