package com.example.aplikacja_pum.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;

import com.example.aplikacja_pum.AddDir.AddActivity;
import com.example.aplikacja_pum.Create.CreateActivity;
import com.example.aplikacja_pum.Home.MainActivity;
import com.example.aplikacja_pum.Login.LoginActivity;
import com.example.aplikacja_pum.Login.RegisterActivity;
import com.example.aplikacja_pum.Profil.ProfilActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Ranking.RankingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHelper";

    @SuppressLint("LongLogTag")
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG,"konfiguracjaNawigiDol: Klasa");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
        bottomNavigationViewEx.setIconSize(30, 30);
    }

    //otwiera nowe okna activity zaleznie od wybranej opcji dolnego menu
    public static void enableNavigation(final Context con, BottomNavigationViewEx bottomNavigationViewEx) {
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home_icon:
                        Intent home = new Intent(con, MainActivity.class);
                        con.startActivity(home);
                        break;

                    case R.id.ranking_icon:
                        Intent ranking = new Intent(con, LoginActivity.class);
                        con.startActivity(ranking);
                        break;

                    case R.id.add_icon:
                        Intent add = new Intent(con, MainActivity.class);
                        con.startActivity(add);
                        break;

                    case R.id.create_icon:
                        Intent create = new Intent(con, RegisterActivity.class);
                        con.startActivity(create);
                        break;

                    case R.id.profil_icon:
                        Intent profil = new Intent(con, ProfilActivity.class);
                        con.startActivity(profil);
                        break;

                    case R.id.back_profil_icon:
                        Intent home1 = new Intent(con, MainActivity.class);
                        con.startActivity(home1);
                        break;
                }
                return false;
            }
        });
    }
}
