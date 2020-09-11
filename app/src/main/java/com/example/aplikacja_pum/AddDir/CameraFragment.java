package com.example.aplikacja_pum.AddDir;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.aplikacja_pum.R;
import com.example.aplikacja_pum.Utils.Permissions;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class CameraFragment extends Fragment {

    private Button openCamera;
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int  CAMERA_REQUEST_CODE = 5;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.d(TAG, "onCreateView: started.");

        Button btnLaunchCamera = (Button) view.findViewById(R.id.cameraOpen);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching camera.");

                if(((AddActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM){
                    if(((AddActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])){
                        Log.d(TAG, "onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(getActivity(), AddActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        ImageView imageView = view.findViewById(R.id.ivClose);
        imageView.setOnClickListener(v -> getActivity().finish());

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: done taking a photo.");
            Log.d(TAG, "onActivityResult: attempting to navigate to final share screen.");
            //navigate to the final share screen to publish photo
        }
    }
}
