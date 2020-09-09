package com.example.aplikacja_pum.Ranking;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.Models.Photo;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/*
public class RankingFragment extends Fragment {
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;

    private Context mContext;

    private ImageView downloadIMG;
    private TextView userNameTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ranking, container, false); //wyswietlanie fragmentu

        mContext = getActivity();
        mFirebaseMethods = new FirebaseMethods(getActivity());

        setupGridView();

        downloadIMG = (ImageView) view.findViewById(R.id.downloadIMG);
        userNameTV = (TextView) view.findViewById(R.id.userNameTV);

        return view;
    }

    public void setupGridView() {

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                    photos.add(singleSnapshot.getValue(Photo.class));
                }

                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i < photos.size(); i++) {
                    imgUrls.add(photos.get(i).getImagePath());
                }

                userNameTV.setText(photos.get(0).getUserId());
                UniversalImageLoader.setImage(imgUrls.get(0),downloadIMG,null,"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}


 */