package com.example.aplikacja_pum.Login;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.R;

public class RegisterActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "Ekran rejestracji!");

    }
}
