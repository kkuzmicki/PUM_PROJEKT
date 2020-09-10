package com.example.aplikacja_pum.Profil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.Login.LoginActivity;
import com.example.aplikacja_pum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignOutFragment extends Fragment
{

    private static final String TAG = "SignOutFragment";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar progressBar;
    private TextView textViewSignOut, textViewSignOutBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);
        textViewSignOutBar = (TextView)view.findViewById(R.id.textViewSignOutBar);
        textViewSignOut = (TextView)view.findViewById(R.id.textViewSignOut);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Button buttonSignOut = (Button)view.findViewById(R.id.buttonSignOut);

        textViewSignOutBar.setTextColor(Color.RED);

        //ukrycie bara oraz info
        progressBar.setVisibility(View.GONE);
        textViewSignOutBar.setVisibility(View.GONE);

        setupFirebaseAuth();

        buttonSignOut.setOnClickListener(v -> {
            //ukrycie przycisku i info
            textViewSignOut.setVisibility(View.GONE);
            buttonSignOut.setVisibility(View.GONE);

            //pokazanie bara oraz info
            progressBar.setVisibility(View.VISIBLE);
            textViewSignOutBar.setVisibility(View.VISIBLE);

            //poprawiono wylogowanie do activity z uzyciem firebase
            mAuth.signOut();

            //kończy aktualne acitivity
            getActivity().finish();
        });

        return view;
    }

    // ustawienie autoryzacji Firebase
    private void setupFirebaseAuth()
    {
        Log.d(TAG, "ustawienie autoryzacji Firebase");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null)
            {
                Log.d(TAG, "onAuthStateChanged: signed in: " + user.getUid());
            }
            else
            {
                //wylogowuje do activity login
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        };
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
