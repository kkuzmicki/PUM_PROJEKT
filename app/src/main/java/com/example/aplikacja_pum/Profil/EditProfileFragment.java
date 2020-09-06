package com.example.aplikacja_pum.Profil;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.AddDir.AddActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.CountryAdapter;
import com.example.aplikacja_pum.Utils.CountryItem;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class EditProfileFragment extends Fragment
{

    private static final String TAG = "EditProfileFragment";
    private ArrayList<CountryItem> mCountryList;
    private CountryAdapter mAdapter;
    private TextView changeProfilePhoto;

    private ImageView mProfilePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);

        setProfileImage();
        initList();

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
        return view;
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
        mCountryList.add(new CountryItem("UK", R.drawable.uk_flag));
        mCountryList.add(new CountryItem("Germany", R.drawable.ger_flag));
    }
}
