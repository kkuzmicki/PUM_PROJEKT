package com.example.aplikacja_pum.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.aplikacja_pum.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GridImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context context, LayoutInflater mInflater, int layoutResource, String mAppend, ArrayList<String> imgURLs) {
        super(context, layoutResource, imgURLs);
        this.mContext = mContext;
        this.mInflater = mInflater;
        this.layoutResource = layoutResource;
        this.mAppend = mAppend;
        this.imgURLs = imgURLs;
    }

    private static class ViewHolder{
        ImageView image;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.gridImageProgressBar);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.gridImageView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mAppend + imgURL, viewHolder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(viewHolder.mProgressBar != null){
                    viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(viewHolder.mProgressBar != null){
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(viewHolder.mProgressBar != null){
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(viewHolder.mProgressBar != null){
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                }

            }
        });
        return convertView;
    }
}
