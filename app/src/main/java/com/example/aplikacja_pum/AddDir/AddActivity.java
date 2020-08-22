package com.example.aplikacja_pum.AddDir;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Gallery;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.Permissions;
import com.example.aplikacja_pum.Utils.SectionsStatePagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class AddActivity extends AppCompatActivity
{

    private static final String TAG = "AddActivity";

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            setupViewPager();
        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }
    }

    private void setupViewPager(){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment(),"gallery");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
    }
    public void verifyPermissions(String[] permissions){

        ActivityCompat.requestPermissions(
                AddActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions){

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission){

        int permissionRequest = ActivityCompat.checkSelfPermission(AddActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        else{
            return true;
        }
    }
}