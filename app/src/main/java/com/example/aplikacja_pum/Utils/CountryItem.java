package com.example.aplikacja_pum.Utils;

public class CountryItem {

    private String mCountryName;
    private int mFlagImage;

    public CountryItem(String mCountryName, int mFlagImage) {
        this.mCountryName = mCountryName;
        this.mFlagImage = mFlagImage;
    }

    public String getmCountryName() {
        return mCountryName;
    }

    public int getmFlagImage() {
        return mFlagImage;
    }
}
