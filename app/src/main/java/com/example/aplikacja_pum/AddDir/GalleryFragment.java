package com.example.aplikacja_pum.AddDir;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.Login.LoginActivity;
import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.FilePaths;
import com.example.aplikacja_pum.Utils.FileSearch;
import com.example.aplikacja_pum.Utils.FirebaseMethods;
import com.example.aplikacja_pum.Utils.GridImageAdapter;
import com.example.aplikacja_pum.Utils.SectionsStatePagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class GalleryFragment extends Fragment {

    private ProgressBar mProgressBar;
    private Spinner directorySpinner;
    private GridView gridView;
    private ImageView galleryImage;

    private static final int NUM_GRID_COLUMNS = 3;

    //lokalizacja img
    private String mAppend = "file:/";
    private String mSelectedImage;
    private ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        gridView = (GridView) view.findViewById(R.id.gridView);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        list = new ArrayList<>();

        ImageView  imageView = (ImageView) view.findViewById(R.id.ivClose);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView textView = (TextView) view.findViewById(R.id.tvAdd);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTitle.class);
                intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                startActivity(intent);
            }
        });
        init();

        return view;
    }

    private void init(){

            //wybor folderu
            FilePaths filePaths = new FilePaths();
        try {
            if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
                list = FileSearch.getDirectoryPaths(filePaths.PICTURES);
            }

            if(FileSearch.getDirectoryPaths(filePaths.CAMERA) != null){
                list = FileSearch.getDirectoryPaths(filePaths.CAMERA);
            }


            list.add(filePaths.CAMERA);
            list.add(filePaths.PICTURES);

            //nowa lista skroconych lokalizacji
            ArrayList<String> directoryNames = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){

                int index = list.get(i).lastIndexOf("/");
                String string = list.get(i).substring(index);
                directoryNames.add(string);
            }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, directoryNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        directorySpinner.setAdapter(arrayAdapter);

        }catch (Exception e){

                Toast.makeText(getActivity(),
                        "No photos ..."+"\n"+" Create in generator :)", LENGTH_SHORT).show();

        }
        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setupGridView(list.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupGridView(String selectedDirectory) {
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //ustawienie kolumn grid
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //tworzenie adaptera do wyswietlania siatki zdj
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);
        //poprawiona metoda, ktora nie laduje null listy dla poczatkowego elementu
        if(imgURLs.size() > 0){
            setImage(imgURLs.get(0),galleryImage,mAppend);
            mSelectedImage = imgURLs.get(0);
        }
        else {
            galleryImage.setImageResource(0);
            Toast.makeText(getActivity(), "This directory is empty...", LENGTH_SHORT).show();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setImage(imgURLs.get(position), galleryImage, mAppend);
                mSelectedImage = imgURLs.get(0);
            }
        });
    }

    private void setImage(String imgURL, ImageView image, String append) {

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
