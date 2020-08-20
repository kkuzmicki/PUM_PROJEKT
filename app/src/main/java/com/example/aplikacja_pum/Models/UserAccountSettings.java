package com.example.aplikacja_pum.Models;

public class UserAccountSettings
{
    private String avatar;
    private String description;
    private long followers;
    private long following;
    private String name;
    private long posts;

    public UserAccountSettings()
    {

    }

    public UserAccountSettings(String avatar, String description, long followers, long following, String name, long posts)
    {
        this.avatar = avatar;
        this.description = description;
        this.followers = followers;
        this.following = following;
        this.name = name;
        this.posts = posts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }
}
