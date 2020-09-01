package com.example.aplikacja_pum.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.widget.Toast.LENGTH_SHORT;

public class ConvertImage {
    public static Bitmap getBitmap(String url){
        File img = new File(url);
        FileInputStream fileInputStream = null;
        Bitmap bitmap = null;

        try{
            fileInputStream = new FileInputStream(img);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
        }catch (FileNotFoundException e){
            Log.d("ConvertImage", "Error convert URL to bitmap" + e.getMessage());
        }finally {
            try{
                fileInputStream.close();
            }catch (IOException e){
                Log.d("ConvertImage", "Error close file" + e.getMessage());
            }
        }
        return bitmap;
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){

        //quality --> 0 do 100 %
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap .CompressFormat.JPEG, quality, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
