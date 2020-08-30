package com.example.aplikacja_pum.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.aplikacja_pum.Models.User;
import com.example.aplikacja_pum.Models.UserAccountSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods
{
    private static final String TAG = "FirebaseMethods";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String userID;

    private Context context;

    public FirebaseMethods(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        this.context = context;


        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public void sendEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }
    }

    public void registerNewEmail(final String email, String password, final String name)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            userID = mAuth.getCurrentUser().getUid();
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean checkIfNameExists(String name, DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "checkIfNameExists: checking if " + name + " already exists.");

        User user = new User();

        for(DataSnapshot ds: dataSnapshot.child(userID).getChildren())
        {
            user.setName(ds.getValue(User.class).getName());

            if(StringModifier.expandName(user.getName()).equals(name))
            {
                Log.d(TAG, "Podany name istnieje!");
                return true;
            }
        }

        return false;
    }

    public void addNewUser(String email, String name)
    {
        Log.d(TAG, "Adding new user!");

        User user = new User(userID, email, name);

        databaseReference.child("users")
            .child(userID)
            .setValue(user);

        UserAccountSettings settings = new UserAccountSettings("", "", 0, 0, name, 0);

        databaseReference.child("user_account_settings")
                .child(userID)
                .setValue(settings);
    }
}
