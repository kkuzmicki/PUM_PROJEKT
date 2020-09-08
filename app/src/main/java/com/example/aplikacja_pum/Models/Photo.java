package com.example.aplikacja_pum.Models;

import com.google.firebase.database.DataSnapshot;

public class Photo {
    private String title;
    private String tags;
    private String dataCreated;
    private String imagePath;
    private String photoId;
    private String userId;
    private int index;

    public Photo() {

    }

    public Photo(String title, String tags, String dataCreated, String imagePath, String photoId, String userId) {
        this.title = title;
        this.tags = tags;
        this.dataCreated = dataCreated;
        this.imagePath = imagePath;
        this.photoId = photoId;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDataCreated() {
        return dataCreated;
    }

    public void setDataCreated(String dataCreated) {
        this.dataCreated = dataCreated;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", tags='" + tags + '\'' +
                ", dataCreated='" + dataCreated + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", photoId='" + photoId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
