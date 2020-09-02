package com.example.aplikacja_pum.Login;

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
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity
{
    private static final String TAG = "RegisterActivity";

    private String name;
    private String email;

    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;
    private Button registerB;
    private ProgressBar registerPB;
    private TextView loadingTV;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

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
        loadingTV = findViewById(R.id.loadingTV);

        loadingTV.setVisibility(View.GONE);
        registerPB.setVisibility(View.GONE);

        firebaseMethods = new FirebaseMethods(RegisterActivity.this);

        registerB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this, "All fields must be filled out!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerPB.setVisibility(View.VISIBLE);
                    loadingTV.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, password, name);
                }
            }
        });
        setupFirebaseAuth();
    }

    // ustawienie autoryzacji Firebase
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "ustawienie autoryzacji Firebase");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuthListener = firebaseAuth -> {
            Log.d(TAG, "onAuthStateChanged");

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null)
            {
                Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        name += "#" + databaseReference.push().getKey().substring(3, 10); // wartosci do zmiany?

                        firebaseMethods.addNewUser(email, name);

                        registerPB.setVisibility(View.GONE);
                        loadingTV.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
            else
            {
                Log.d(TAG, "onAuthStateChanged: signed out");
            }

            if(mAuth.getCurrentUser() != null)
            {
                Log.d(TAG, "ZMIANA EKRANU");

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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
