package com.database_termproject.twitter.data;

public class User {
    public String user_id;
    public String nickname;
    public String image;

    public User(String user_id, String nickname, String image){
        this.user_id = user_id;
        this.nickname = nickname;
        this.image = image;
    }
}
