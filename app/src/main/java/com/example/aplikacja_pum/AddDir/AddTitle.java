package com.example.aplikacja_pum.AddDir;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddTitle extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String mAppend = "file:/";
    private ImageView closeActivity;
    private TextView add;
    private EditText tags;
    private EditText title;
    private int imageCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        Log.d("AddTitle.class","A: tytul | sciezka: " + getIntent().getStringExtra(getString(R.string.selected_image)));

        mFirebaseMethods = new FirebaseMethods(AddTitle.this);

        tags = (EditText) findViewById(R.id.tagTV);
        title = (EditText) findViewById(R.id.titleTV);

        setupFirebaseAuth();
        closeActivity();
        AddToImage();
        ShowImage();
    }

    private void closeActivity(){
        closeActivity = (ImageView) findViewById(R.id.ivClose);
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void AddToImage(){
        add = (TextView) findViewById(R.id.addtoolbartv);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dodaje zdj na serwer
            }
        });
    }

    private void ShowImage(){
        Intent intent = getIntent();
        ImageView imageView = (ImageView) findViewById(R.id.imageToAdd);
        UniversalImageLoader.setImage(intent.getStringExtra(getString(R.string.selected_image)), imageView,null, mAppend);
    }

    //--------------------laczenie z baza oraz dodanie zdjecia--------------------

    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null)
                {
                    //zalogowano
                    Log.d("AddTitle","User: " + user.getUid());
                }else {

                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageCount = mFirebaseMethods.getImageCount(snapshot);
                Log.d("AddTitile(Count IMG)", "Count IMG: " + imageCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
