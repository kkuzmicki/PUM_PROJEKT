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
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


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

    TextView description;
    TextView display_name;
    TextView textViewPosts;
    TextView textViewFollowers;
    TextView textViewFollowings;
    TextView nationalityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();
        setupIntent();
        setupActivityWidgets();
        setProfileImage();
        firebaseMethods = new FirebaseMethods(this);

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
                Log.d(TAG, "user not null");
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

                nationalityTV = findViewById(R.id.nationalityTV);
                nationalityTV.setText("Nationality: " + userInfo.getUserAccountSettings().getNationality());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setProfileImage()
    {
        Log.d(TAG, "setProfileImage: setting profile photo.");
        String imgURL = "i0.wp.com/www.apkspree.com/wp-content/uploads/2019/11/com.TailOfTales.WaifuOrLaifu-logo.png?fit=512%2C512&ssl=1";
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "https://");
    }

    private void setupActivityWidgets()
    {
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = (ImageView) findViewById(R.id.profile_photo);


    }


    private void setupIntent()
    {

        ImageView profileMenu = (ImageView) findViewById(R.id.settingsMenu);
        profileMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        ImageView profileImages = (ImageView) findViewById(R.id.profilePhotoGridView);
        profileImages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: navigating to profile images.");
                Intent intent = new Intent(mContext, ProfilePhotosActivity.class);
                startActivity(intent);
            }
        });
    }




    //nawigacja dolna
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
