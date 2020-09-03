package com.example.aplikacja_pum.AddDir;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.aplikacja_pum.Create.CreateActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.Permissions;
import com.example.aplikacja_pum.Utils.SectionsStatePagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class AddActivity extends AppCompatActivity
{
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    private static final String TAG = "AddActivity" ;

    private ViewPager viewPager;

    protected SectionsStatePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            //zaakceptowane wczesniej
            setupViewPager();
        }else{
            //weryfikacja
            verifyPermissions(Permissions.PERMISSIONS);
            //poprawiono akceptacje
            finish();
        }
    }

    private void setupViewPager(){
        adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment(),"gallery");
        //wykorzystac adapter cam
        // (wprowadzono do generatora memow) !!!
        //adapter.addFragment(new CameraFragment(),"generator");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        //tabLayout.getTabAt(1).setText("Camera");

        File fileCamera = new File("/storage/emulated/0/DCIM/camera/");
        File filePictures = new File("/storage/emulated/0/DCIM/pictures/");

        if(fileCamera.getTotalSpace() == 0 && filePictures.getTotalSpace() == 0){
            Intent intent = new Intent(AddActivity.this, CreateActivity.class);
            startActivity(intent);
        }
    }


    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                AddActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    //zapytanie
    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(AddActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }
    public int getCurrentTabNumber(){
        return viewPager.getCurrentItem();
    }
}