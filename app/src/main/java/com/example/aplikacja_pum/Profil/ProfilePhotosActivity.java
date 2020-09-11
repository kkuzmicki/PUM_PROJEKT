package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.AddDir.AddActivity;
import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.Models.User;
import com.example.aplikacja_pum.Models.UserAccountSettings;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.GridImageAdapter;
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
import java.util.Random;

public class ProfilePhotosActivity extends AppCompatActivity
{
    private static final String TAG = "ProfilePhotosActivity";
    private Context mContext;
    private static final int NUM_GRID_COLUMNS = 2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView profilePictures;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photogridview);
        mContext = ProfilePhotosActivity.this;
        Log.d(TAG, "onCreate: started.");

        setUpBottomNavigationViev();
        tempGridSetup();
        profilePictures = findViewById(R.id.profilePictures);


        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Log.d(TAG, "onClick: Navigating back to 'ProfileActivity'.");
            finish();
        });
    }

    private void tempGridSetup()
    {
        ArrayList<Photo> photos = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child("user_photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                        photos.add(singleSnapshot.getValue(Photo.class));
                    }
                    Log.d("PPA", "singleSnapshot_size: " + photos.size());

                    if(photos.size() > 0){

                        ArrayList<String> url = new ArrayList<>();

                        for (int i = 0; i < photos.size(); i++) {
                            //tmp do wyswietlania od tylu IMG
                            int tmp = photos.size() - 1 - i;

                            url.add(photos.get(tmp).getImagePath());
                        }

                        setupImageGrid(url);
                        Log.d("test: ", url.get(0));

                    }else {
                        Toast.makeText(ProfilePhotosActivity.this, "Firstly, you need to add some photo. " + "\nYou have been moved to adding screen!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(ProfilePhotosActivity.this, AddActivity.class);
                        startActivity(intent);
                    }
                }catch (NullPointerException e){
                    Log.d("Error: ", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupImageGrid(ArrayList<String> imgURLs) {

        GridView gridView = findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
    }

    private void setUpBottomNavigationViev()
    {
        Log.d(TAG,"konfiguracjaNawigiDol");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

}
