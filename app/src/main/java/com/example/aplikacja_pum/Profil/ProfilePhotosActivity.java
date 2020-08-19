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

public class ProfilePhotosActivity extends AppCompatActivity
{

    private static final String TAG = "ProfilePhotosActivity";
    private static final int ActivityNumber = 4;
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
        tempGridSetup();


        ImageView backArrow = (ImageView) findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Navigating back to 'ProfileActivity'.");
                finish();
            }
        });
    }


    private void tempGridSetup()
    {
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://wallpaperaccess.com/full/1851786.jpg");
        imgURLs.add("https://images.unsplash.com/photo-1506744038136-46273834b3fb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjI0MX0&w=1000&q=80");
        imgURLs.add("https://www.lukas-petereit.com/wp-content/uploads/2018/08/Sunset-at-Verdon-Canyon-Landscape-of-Provence-Photography-in-France.jpg");
        imgURLs.add("https://lh3.googleusercontent.com/proxy/Anzw4adphSn1_clN_bTqVDo-gG4gMG2Y2LB3igul7q6PRX5Dzja0yKUV46HExyzHpsocnvvNYnrItLfHkH7BPtG1D51jg78dNlONmlGvmk0");
        imgURLs.add("https://wallpaperaccess.com/full/3022618.jpg");
        imgURLs.add("https://images.pexels.com/photos/414102/pexels-photo-414102.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        imgURLs.add("https://www.planetware.com/wpimages/2020/02/france-in-pictures-beautiful-places-to-photograph-eiffel-tower.jpg");
        imgURLs.add("https://static.vecteezy.com/system/resources/previews/000/144/435/non_2x/beautiful-landscape-scene-vector.jpg");

        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(ArrayList<String> imgURLs)
    {
        GridView gridView = (GridView) findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_imageview, "", imgURLs);
        gridView.setAdapter(adapter);
    }

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
