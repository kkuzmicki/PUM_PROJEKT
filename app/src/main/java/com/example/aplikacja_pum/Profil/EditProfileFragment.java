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
import com.google.firebase.auth.FirebaseUser;
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

    Spinner spinnerCountries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = view.findViewById(R.id.profile_photo);

        usernameET = view.findViewById(R.id.username);
        descriptionET = view.findViewById(R.id.description);
        emailET = view.findViewById(R.id.email);
        saveSettingsButton = view.findViewById(R.id.saveSettingsButton);

        saveSettingsButton.setOnClickListener(v -> saveProfileSettings());

        setProfileImage();
        initList();
        setupFirebase();
        init();

        spinnerCountries = view.findViewById(R.id.spnCountry);
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

        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Log.d(TAG, "onClick: navigating back to ProfileActivity");
            getActivity().finish();
        });

        changeProfilePhoto = view.findViewById(R.id.changeProfilePhoto);
        changeProfilePhoto.setOnClickListener(v -> {
            Intent intent  = new Intent(getActivity(), AddActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
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

                switch(userInfo.getUserAccountSettings().getNationality())
                {
                    case "PL":
                        spinnerCountries.setSelection(0);
                        break;
                    case "UK":
                        spinnerCountries.setSelection(1);
                        break;
                    case "DE":
                        spinnerCountries.setSelection(2);
                        break;
                }
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
        switch(spinnerCountries.getSelectedItemPosition())
        {
            case 0:
                databaseReference.child("user_account_settings").child(userID).child("nationality").setValue("PL");
                break;
            case 1:
                databaseReference.child("user_account_settings").child(userID).child("nationality").setValue("UK");
                break;
            case 2:
                databaseReference.child("user_account_settings").child(userID).child("nationality").setValue("DE");
                break;
        }
        if(name.indexOf('#') != -1)
        {
            Toast.makeText(super.getContext(), "'#' symbol is not allowed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(name.indexOf(' ') != -1)
        {
            Toast.makeText(super.getContext(), "Spaces are not allowed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(name.isEmpty())
        {
            Toast.makeText(super.getContext(), "'Name' field can not be empty!'", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!name.equals(username))
        {
            name += "#" + databaseReference.push().getKey().substring(3, 10);
            databaseReference.child("user_account_settings").child(userID).child("name").setValue(name);
            databaseReference.child("users").child(userID).child("name").setValue(name);
        }
        Toast.makeText(super.getContext(), "Changes saved!", Toast.LENGTH_SHORT).show();
    }

    private void setProfileImage()
    {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if(user != null)
            {
                Log.d(TAG, "user not null");
            }
            else
            {
                Log.d(TAG, "user null");
            }
        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserInfo userInfo = firebaseMethods.getUserInfo(snapshot);

                if(userInfo.getUserAccountSettings().getAvatar().isEmpty()){
                    UniversalImageLoader.setImage("https://www.google.pl/search?q=brak+zdj%C4%99cia&sxsrf=ALeKk02LSZoExK6u75370cHhEQC9AOEMYA:1599657985446&source=lnms&tbm=isch&sa=X&ved=2ahUKEwiD7O-vltzrAhXGlIsKHezZDJ4Q_AUoAXoECA0QAw&biw=1023&bih=740&dpr=1.25#imgrc=6GaYxqHRw9gTqM", mProfilePhoto, null, "");
                }else {
                    UniversalImageLoader.setImage(userInfo.getUserAccountSettings().getAvatar(), mProfilePhoto, null, "");
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initList(){
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem("Poland", R.drawable.pl_flag));
        mCountryList.add(new CountryItem("Great Britain", R.drawable.uk_flag));
        mCountryList.add(new CountryItem("Germany", R.drawable.ger_flag));
    }
}
