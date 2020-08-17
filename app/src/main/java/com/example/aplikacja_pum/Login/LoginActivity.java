package com.example.aplikacja_pum.Login;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.R;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private ProgressBar loginPB;
    private EditText emailET;
    private EditText passwordET;
    private TextView pleaseWaitTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        Log.d(TAG, "Ekran logowania!");
    }

    private void init()
    {
        //loginPB = findViewById(R.id.)
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

    }
}
