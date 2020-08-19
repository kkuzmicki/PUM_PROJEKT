package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.GridImageAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class ProfilePhotosActivity extends AppCompatActivity {

    private static final String TAG = "ProfilePhotosActivity";
    private static final int ActivityNumber = 4;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogridview);
        mContext = ProfilePhotosActivity.this;
        Log.d(TAG, "onCreate: started.");

        setUpBottomNavigationViev();
        tempGridSetup();


        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Navigating back to 'ProfileActivity'.");
                finish();
            }
        });
    }


    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://wallpaperaccess.com/full/1851786.jpg");

        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView) findViewById(R.id.gridView);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
    }

    private void setUpBottomNavigationViev() {
        Log.d(TAG,"konfiguracjaNawigiDol");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }
}
