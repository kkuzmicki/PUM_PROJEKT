package com.example.aplikacja_pum.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplikacja_pum.Home.MainActivity;
import com.example.aplikacja_pum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "LoginActivity";

    private ProgressBar loginPB;
    private EditText emailET;
    private EditText passwordET;
    private TextView loadingTV;
    private TextView registerTV;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "Ekran logowania!");
        setupFirebaseAuth();
        init();
        context = this;
    }

    private void init()
    {
        loginPB = findViewById(R.id.loginPB);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        loadingTV = findViewById(R.id.loadingTV);
        registerTV = findViewById(R.id.registerTV);
        loginPB.setVisibility(View.GONE);
        loadingTV.setVisibility(View.GONE);

        Button loginB = findViewById(R.id.loginB);

        registerTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "You must fill out all the fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loginPB.setVisibility(View.VISIBLE);
                    loadingTV.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                        FirebaseUser user = mAuth.getCurrentUser();
                        try {
                            if(user.isEmailVerified())
                            {
                                Log.d(TAG, "mail zweryfikowany!!!");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(context, "mail niezweryfikowany", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        }
                        catch (NullPointerException e)
                        {
                            Log.e(TAG, "wyjatek NullPointException");
                        }

                        if(!task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Logging failed!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Logging successed!", Toast.LENGTH_SHORT).show();
                        }
                        loginPB.setVisibility(View.GONE);
                        loadingTV.setVisibility(View.GONE);
                    });
                }
                Log.d(TAG, "KLIK!");
            }
        });
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
                if(user != null)
                {
                    Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }

                if(mAuth.getCurrentUser() != null)
                {
                    Log.d(TAG, "ZMIANA EKRANU");

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
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
}