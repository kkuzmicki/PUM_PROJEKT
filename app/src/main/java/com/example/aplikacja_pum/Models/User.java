package com.example.aplikacja_pum.Models;

public class User
{
    private String user_id;
    private String email;
    private String name;

    public User(String user_id, String email, String name)
    {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
    }

    public User()
    {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
