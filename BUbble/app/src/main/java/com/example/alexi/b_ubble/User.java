package com.example.alexi.b_ubble;

/**
 * Created by alexi on 30/01/2018.
 */

public class User {

    private String username;
    private String uid;
    private String mail;

    public User() {
    }

    public User(String username, String uid, String mail) {
        this.username = username;
        this.uid = uid;
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
