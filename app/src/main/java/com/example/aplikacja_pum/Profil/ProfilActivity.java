package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;


public class ProfilActivity extends AppCompatActivity {
    private static final String TAG = "ProfilActivity";
    private static final int ActivityNumber = 4;

    private Context mContext = ProfilActivity.this;

    private ProgressBar mProgressBar;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();
        setupIntent();
        setupActivityWidgets();
        setProfileImage();
    }



    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile photo.");
        String imgURL = "i0.wp.com/www.apkspree.com/wp-content/uploads/2019/11/com.TailOfTales.WaifuOrLaifu-logo.png?fit=512%2C512&ssl=1";
        UniversalImageLoader.setImage(imgURL, profilePhoto, mProgressBar, "https://");
    }

    private void setupActivityWidgets(){
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = (ImageView) findViewById(R.id.profile_photo);
    }


    private void setupIntent(){

        ImageView profileMenu = (ImageView) findViewById(R.id.settingsMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }




    //nawigacja dolna
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ActivityNumber);
        menuItem.setChecked(true);
    }
}
