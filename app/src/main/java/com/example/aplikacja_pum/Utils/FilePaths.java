package com.example.aplikacja_pum.Utils;

import android.os.Environment;

public class FilePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";
    public String MEME = ROOT_DIR + "/DCIM/MEME";

    public String FIREBASE_IMAGE_STORAGE = "photos/users/";
}
