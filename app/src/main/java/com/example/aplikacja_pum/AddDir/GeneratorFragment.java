package com.example.aplikacja_pum.AddDir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.R;

public class GeneratorFragment extends Fragment {

    private ProgressBar mProgressBar;
    private Spinner directorySpinner;
    private GridView gridView;
    private ImageView galleryImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_generator, container, false); //wyswietlanie fragmentu
        return view;
    }
}
