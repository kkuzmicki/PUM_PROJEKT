package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.AddDir.AddActivity;
import com.example.aplikacja_pum.Models.User;
import com.example.aplikacja_pum.Models.UserInfo;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.CountryAdapter;
import com.example.aplikacja_pum.Utils.CountryItem;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class EditProfileFragment extends Fragment
{
    private static final String TAG = "EditProfileFragment";
    private ArrayList<CountryItem> mCountryList;
    private CountryAdapter mAdapter;
    private TextView changeProfilePhoto;

    private ImageView mProfilePhoto;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    EditText usernameET;
    EditText descriptionET;
    EditText emailET;

    TextView saveSettingsButton;

    private String userID;
    String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);

        usernameET = view.findViewById(R.id.username);
        descriptionET = view.findViewById(R.id.description);
        emailET = view.findViewById(R.id.email);
        saveSettingsButton = view.findViewById(R.id.saveSettingsButton);

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });

        setProfileImage();
        initList();
        setupFirebase();
        init();

        Spinner spinnerCountries = (Spinner) view.findViewById(R.id.spnCountry);
        mAdapter = new CountryAdapter(this.getActivity(), mCountryList);
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                getActivity().finish();
            }
        });

        changeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        firebaseMethods = new FirebaseMethods(super.getContext());

        return view;
    }

    private void init()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "TEST ZDANY");

                UserInfo userInfo = firebaseMethods.getUserInfo(snapshot);

                username = userInfo.getUserAccountSettings().getName();
                username = username.substring(0, username.indexOf("#"));
                usernameET.setText(username);
                descriptionET.setText(userInfo.getUserAccountSettings().getDescription());
                emailET.setText(userInfo.getUser().getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupFirebase()
    {
        Log.d(TAG, "ustawienie autoryzacji Firebase");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        userID = mAuth.getCurrentUser().getUid();
    }

    private void saveProfileSettings()
    {
        String name = usernameET.getText().toString();
        final String description = descriptionET.getText().toString();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User();
                for(DataSnapshot ds: snapshot.child("users").getChildren())
                {
                    if(ds.getKey().equals(userID))
                    {
                        user.setName(ds.getValue(User.class).getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("user_account_settings").child(userID).child("description").setValue(description);
        if(name.indexOf('#') != -1)
        {
            Toast.makeText(super.getContext(), "'#' symbol is not allowed!", Toast.LENGTH_SHORT).show();
        }
        else if(name.indexOf(' ') != -1)
        {
            Toast.makeText(super.getContext(), "Spaces are not allowed!", Toast.LENGTH_SHORT).show();
        }
        else if(!name.equals(username))
        {
            name += "#" + databaseReference.push().getKey().substring(3, 10);
            databaseReference.child("user_account_settings").child(userID).child("name").setValue(name);
            databaseReference.child("users").child(userID).child("name").setValue(name);
        }

    }

    private void setProfileImage()
    {
        Log.d(TAG, "setProfileImage: setting profile image.");
        String imgURL = "i0.wp.com/www.apkspree.com/wp-content/uploads/2019/11/com.TailOfTales.WaifuOrLaifu-logo.png?fit=512%2C512&ssl=1";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");
    }

    private void initList(){
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem("Poland", R.drawable.pl_flag));
        mCountryList.add(new CountryItem("Great Britain", R.drawable.uk_flag));
        mCountryList.add(new CountryItem("Germany", R.drawable.ger_flag));
    }
}
