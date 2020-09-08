package com.example.aplikacja_pum.Ranking;

import android.content.Context;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static android.widget.Toast.LENGTH_SHORT;

public class RankingActivity extends AppCompatActivity
{

    private static final String TAG = "RankingActivity";
    private static final int ActivityNumber = 1;

    private Context mContext = RankingActivity.this;

    private ImageView downloadIMG;
    private TextView userNameTV;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Log.d(TAG, "onCreate: starting.");

        setupBottomNavigationView();

        downloadIMG = (ImageView) findViewById(R.id.downloadIMG);
        userNameTV = (TextView) findViewById(R.id.userNameTV);


    }



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
}
