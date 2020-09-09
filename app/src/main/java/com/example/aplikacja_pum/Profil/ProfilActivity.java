package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.Models.UserAccountSettings;
import com.example.aplikacja_pum.Models.UserInfo;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;


public class ProfilActivity extends AppCompatActivity
{
    private static final String TAG = "ProfilActivity";
    private static final int ActivityNumber = 4;

    private Context mContext = ProfilActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private TextView description;
    private TextView display_name;
    private TextView textViewPosts;
    private TextView textViewFollowers;
    private TextView textViewFollowings;
    private TextView profileName;
    private TextView nationalityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();
        setupIntent();
        setupActivityWidgets();
        //setProfileImage();
        firebaseMethods = new FirebaseMethods(this);
        nationalityTV = findViewById(R.id.nationalityTV);
        setupFirebaseAuth();
    }

    private void setupFirebaseAuth()
    {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if(user != null)
            {

            }
            else
            {
                Log.d(TAG, "user null");
            }
        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "PRZED USERINFO");
                UserInfo userInfo = firebaseMethods.getUserInfo(snapshot);

                description = findViewById(R.id.description);
                description.setText(userInfo.getUserAccountSettings().getDescription());

                display_name = findViewById(R.id.display_name);
                String usernameString = userInfo.getUserAccountSettings().getName();
                usernameString = usernameString.substring(0, usernameString.indexOf("#"));
                display_name.setText(usernameString);

                textViewPosts = findViewById(R.id.textViewPosts);
                textViewPosts.setText(Long.toString(userInfo.getUserAccountSettings().getPosts()));

                textViewFollowers = findViewById(R.id.textViewFollowers);
                textViewFollowers.setText(Long.toString(userInfo.getUserAccountSettings().getFollowers()));

                textViewFollowings = findViewById(R.id.textViewFollowings);
                textViewFollowings.setText(Long.toString(userInfo.getUserAccountSettings().getFollowing()));

                profileName = findViewById(R.id.profileName);
                profileName.setText(usernameString);

                nationalityTV.setText("Nationality: " + userInfo.getUserAccountSettings().getNationality());

                if(userInfo.getUserAccountSettings().getAvatar().isEmpty()){
                    UniversalImageLoader.setImage("https://www.google.pl/search?q=brak+zdj%C4%99cia&sxsrf=ALeKk02LSZoExK6u75370cHhEQC9AOEMYA:1599657985446&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiD7O-vltzrAhXGlIsKHezZDJ4Q_AUoAXoECA0QAw&biw=1023&bih=740&dpr=1.25#imgrc=6GaYxqHRw9gTqM", profilePhoto, mProgressBar, "");
                }else {
                    UniversalImageLoader.setImage(userInfo.getUserAccountSettings().getAvatar(), profilePhoto, mProgressBar, "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
/*
    private void setProfileImage()
    {
        Log.d(TAG, "setProfileImage: setting profile photo.");



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("user_account_settings")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            List <UserAccountSettings> userAccountSettings;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    userAccountSettings = new ArrayList<>();
                    userAccountSettings.add(singleSnapshot.getValue(UserAccountSettings.class));
                }
                if(userAccountSettings.get(0).getAvatar().isEmpty()){
                    //UniversalImageLoader.setImage(userAccountSettings.get(0).getAvatar(), profilePhoto, mProgressBar, ""); //zmienic url
                }else {
                    UniversalImageLoader.setImage(userAccountSettings.get(0).getAvatar(), profilePhoto, mProgressBar, ""); //zmienic url
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


 */
    private void setupActivityWidgets()
    {
        mProgressBar = findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = findViewById(R.id.profile_photo);
    }

    private void setupIntent()
    {

        ImageView profileMenu = findViewById(R.id.settingsMenu);
        profileMenu.setOnClickListener(v -> {
            Log.d(TAG, "onClick: navigating to account settings.");
            Intent intent = new Intent(mContext, AccountSettingsActivity.class);
            startActivity(intent);
        });

        ImageView profileImages = findViewById(R.id.profilePhotoGridView);
        profileImages.setOnClickListener(v -> {
            Log.d(TAG, "onClick: navigating to profile images.");
            Intent intent = new Intent(mContext, ProfilePhotosActivity.class);
            startActivity(intent);
        });
    }

    // nawigacja dolna
    private void setupBottomNavigationView()
    {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ActivityNumber);
        menuItem.setChecked(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
