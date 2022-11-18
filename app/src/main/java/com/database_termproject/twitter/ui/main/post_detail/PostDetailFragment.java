package com.database_termproject.twitter.ui.main.post_detail;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.database_termproject.twitter.data.Comment;
import com.database_termproject.twitter.data.Post;
import com.database_termproject.twitter.databinding.FragmentPostDetailBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.adapter.CommentRVAdapter;
import com.database_termproject.twitter.ui.adapter.PostImageRVAdapter;
import com.database_termproject.twitter.ui.main.home.HomePostRVAdapter;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PostDetailFragment extends BaseFragment<FragmentPostDetailBinding> {
    Post post;
    String comment = "";

    CommentRVAdapter commentRVAdapter;
    @Override
    protected FragmentPostDetailBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentPostDetailBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        // Post 기본 정보 바인딩,
        String postJson = PostDetailFragmentArgs.fromBundle(getArguments()).getPostId();
        post = new Gson().fromJson(postJson, Post.class);

        binding.postDetailProfileTv.setText(post.nickname);
        binding.postDetailIdTv.setText("@"+post.writer_id);
        binding.postDetailContentTv.setText(post.content);
        binding.postDetailDateTv.setText(post.written_date);
        String meta = "리트윗 " + post.retweet_num + "회 " + "마음에 들어요 " + post.num_of_likes + "회";
        binding.postDetailMetaTv.setText(meta);

        if(!post.fileList.isEmpty()){ // 이미지 있는 경우, AlbumRV
            PostImageRVAdapter homePostImageRVAdapter = new PostImageRVAdapter(requireContext(), post.fileList);
            binding.postDetailFileRv.setAdapter(homePostImageRVAdapter);
        }

        // Comment 조회,
        commentRVAdapter = new CommentRVAdapter(requireContext());
        binding.postDetailCommentRv.setAdapter(commentRVAdapter);

        new GetCommentAsyncTask().execute();

        // Event Listeners
        binding.postDetailTweetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = binding.postDetailCommentEt.getText().toString();

                if(comment.equals("") || comment.length() > 45){
                    showToast("댓글은 1~45자 이내어야 합니다.");
                    return;
                }

                new WriteCommentAsyncTask().execute();
            }
        });

    }

    // 댓글 달기 JDBC
    @SuppressLint("StaticFieldLeak")
    public class WriteCommentAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String s2 = "insert into comment (post_id, content, user_id) values ('"+ post.post_id + "', '" + comment + "', '" + "yusin" +"');";
                PreparedStatement pstm = connection.prepareStatement(s2);
                pstm.executeUpdate();

                return true;
            } catch (Exception e) {
                Log.e("WriteCommentAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) { // 성공, 댓글 새로고침 & et 비우기
                showToast("Comment is uploaded!");
                binding.postDetailCommentEt.setText("");
                comment = "";

                new GetCommentAsyncTask().execute();
            }

            this.cancel(true);
        }
    }

    // 댓글 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetCommentAsyncTask extends AsyncTask<Void, Void, ArrayList<Comment>> {
        @Override
        protected ArrayList<Comment> doInBackground(Void... voids) {
            ArrayList<Comment> commentList = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                Statement stmt = connection.createStatement();
                String s2 = "select comment_id, user_id, nickname, content, num_of_likes from comment as C join user as U where post_id = " + post.post_id + " and C.user_id = U.id;";
                ResultSet rs = stmt.executeQuery(s2);

                while(rs.next()){
                    Comment comment = new Comment(rs.getInt("comment_id"), rs.getString("user_id"), rs.getString("nickname"), rs.getString("content"), rs.getInt("num_of_likes"));
                    commentList.add(comment);
                }

            } catch (Exception e) {
                Log.e("WriteCommentAsyncTask", "Error reading school information", e);
            }

            return commentList;
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> result) {
            if (!result.isEmpty()) {

                commentRVAdapter.addComments(result);
            }

            this.cancel(true);
        }
    }
}
