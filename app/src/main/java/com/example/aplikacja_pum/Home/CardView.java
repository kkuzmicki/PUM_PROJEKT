package com.example.aplikacja_pum.Home;

public class CardView {
    private String url;
    private String time;
    private String title;

    public CardView(String url, String time, String title) {
        this.url = url;
        this.time = time;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
