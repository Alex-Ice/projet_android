package com.example.alexi.b_ubble;

/**
 * Created by alexi on 27/01/2018.
 */

public class Message {
    private String content, username,date;

    //Default constructor
    public Message() {
    }

    //Constructor
    public Message(String content, String username, String date){
        this.content = content;
        this.username = username;
        this.date = date;
    }

    //Date getter
    public String getDate(){return date;}

    //Date setter
    public void setDate(String date){
        this.date = date;
    }

    //Username getter
    public String getUsername(){return username;}

    //Username setter
    public void setUsername(String username){
        this.username = username;
    }

    //Content getter
    public String getContent(){
        return content;
    }

    //Content setter
    public void setContent(String content){
        this.content = content;
    }
}
