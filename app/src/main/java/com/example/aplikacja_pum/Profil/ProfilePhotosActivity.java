package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.GridImageAdapter;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Random;

public class ProfilePhotosActivity extends AppCompatActivity
{
    private static final String TAG = "ProfilePhotosActivity";
    private Context mContext;
    private static final int NUM_GRID_COLUMNS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogridview);
        mContext = ProfilePhotosActivity.this;
        Log.d(TAG, "onCreate: started.");

        setUpBottomNavigationViev();
        //tempGridSetup();


        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Navigating back to 'ProfileActivity'.");
            finish();
        });
    }

    private void tempGridSetup()
    {
        final ArrayList<Photo> photos = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child("user_photos");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    photos.add(singleSnapshot.getValue(Photo.class));
                }

                ArrayList<String> url = new ArrayList<>();

                for (int i = 0; i < photos.size(); i++) {
                    //url.add(photos.get(i).getImagePath());
                }

                //setupImageGrid(url);
                 Log.d("test: ", url.get(0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
/*
    private void setupImageGrid(ArrayList<String> imgURLs) {

        GridView gridView = (GridView) findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
    }
*/
    private void setUpBottomNavigationViev()
    {
        Log.d(TAG,"konfiguracjaNawigiDol");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }
}
