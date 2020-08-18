package com.example.aplikacja_pum.Login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.R;

public class RegisterActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;
    private Button registerB;
    private ProgressBar registerPB;
    private TextView loadingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "Ekran rejestracji!");
        init();
    }

    private void init()
    {
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        registerB = findViewById(R.id.registerB);
        registerPB = findViewById(R.id.registerPB);
        //Button registerB = findViewById(R.id.registerB);
        loadingTV = findViewById(R.id.loadingTV);

        loadingTV.setVisibility(View.GONE);
        registerPB.setVisibility(View.GONE);

        registerB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}