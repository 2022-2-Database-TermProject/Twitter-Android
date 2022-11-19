package com.database_termproject.twitter.data;

public class Comment {
    public int comment_id;
    public String writer_id;
    public String nickname;
    public String content;
    public int num_of_likes;

    public Comment(int comment_id, String writer_id, String nickname, String content, int num_of_likes){
        this.comment_id = comment_id;
        this.writer_id = writer_id;
        this.nickname = nickname;
        this.content = content;
        this.num_of_likes = num_of_likes;
    }
}
