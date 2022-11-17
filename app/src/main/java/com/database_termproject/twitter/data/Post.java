package com.database_termproject.twitter.data;

import java.util.ArrayList;

public class Post {
    public int post_id;
    public String writer_id;
    public String nickname;
    public String content;
    public ArrayList<String> fileList;
    public String written_date;
    public int num_of_likes;
    public int retweet_num;

    public Post(int post_id, String writer_id, String nickname, String content, ArrayList<String> fileList, String written_date, int num_of_likes, int retweet_num){
        this.post_id = post_id;
        this.writer_id = writer_id;
        this.nickname = nickname;
        this.content = content;
        this.fileList = fileList;
        this.written_date = written_date;
        this.num_of_likes = num_of_likes;
        this.retweet_num = retweet_num;
    }
}
