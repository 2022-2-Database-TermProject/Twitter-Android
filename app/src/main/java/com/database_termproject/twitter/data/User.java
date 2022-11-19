package com.database_termproject.twitter.data;

public class User {
    public String user_id;
    public String nickname;
    public String image;
    public int following_state = 0; // 기본(0), 팔로잉(1), 대기 중(2)

    public User(String user_id, String nickname, String image){
        this.user_id = user_id;
        this.nickname = nickname;
        this.image = image;
    }

    public void setFollowing(int state){
        this.following_state = state;
    }
}
