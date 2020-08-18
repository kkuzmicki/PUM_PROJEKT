package com.example.aplikacja_pum.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aplikacja_pum.Login.LoginActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.BottomNavigationViewHelper;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"Zaczynamy!");
        initImageLoader();
        setUpBottomNavigationViev();
        setupFirebaseAuth();
    }

    // ustawienie autoryzacji Firebase
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "ustawienie autoryzacji Firebase");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user); // czy uzytkownik zalogowany
                if(user != null)
                {
                    Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void checkCurrentUser(FirebaseUser user)
    {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in");

        if(user == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
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

    private void setUpBottomNavigationViev() {
        Log.d(TAG,"konfiguracjaNawigiDol");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx)findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(MainActivity.this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }
}
