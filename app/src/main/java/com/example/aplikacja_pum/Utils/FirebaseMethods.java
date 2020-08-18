package com.example.aplikacja_pum.Utils;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseMethods
{
    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context context;

    public FirebaseMethods(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        this.context = context;

        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void registerNewEmail(final String email, String password, final String name)
    {

    }
}
