package com.database_termproject.twitter.ui.main.home;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;

import com.database_termproject.twitter.data.Post;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.databinding.FragmentHomeBinding;
import com.database_termproject.twitter.ui.adapter.PostRVAdapter;
import com.database_termproject.twitter.ui.main.post_detail.PostDetailFragment;
import com.database_termproject.twitter.ui.post.PostActivity;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    PostRVAdapter homePostRVAdapter;

    @Override
    protected FragmentHomeBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        new GetPostAsyncTask().execute();
    }

    @Override
    protected void initAfterBinding() {
        // Click listener
        binding.homeNewpostIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), PostActivity.class));
            }
        });

        homePostRVAdapter = new PostRVAdapter(requireContext());
        binding.homePostRv.setAdapter(homePostRVAdapter);
        homePostRVAdapter.setMyClickListener(new PostRVAdapter.MyItemClickListener() {
            @Override
            public void like(@NonNull Post post) { // 좋아요 클릭 시, 게시물 좋아요 JDBC
                new LikePostAsyncTask().execute(post.post_id);
            }

            @Override
            public void retweet(@NonNull Post post) { // 리트윗 클릭 시, 리트윗 액티비티로 이동

            }

            @Override
            public void onClick(@NonNull Post post) { // 클릭 시, 포스트 상세 보기로 이동
                String postJson = new Gson().toJson(post);
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(postJson);
                findNavController().navigate(action);
            }
        });
    }

    // 포스트 조회
    @SuppressLint("StaticFieldLeak")
    public class GetPostAsyncTask extends AsyncTask<Void, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(Void... voids) {
            ArrayList<Post> postList = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select * from post, user where post.writer_id = user.id order by written_date desc; ";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int post_id = resultSet.getInt("post_id");
                    sql = "select * from file where post_id = " + post_id;
                    PreparedStatement statement1 = connection.prepareStatement(sql);
                    ResultSet resultSet2 = statement1.executeQuery();

                    ArrayList<String> fileList = new ArrayList<>();
                    while(resultSet2.next()){
                        String file = resultSet2.getString("file");
                        fileList.add(file);
                    }

                    Post post = new Post(resultSet.getInt("post_id"), resultSet.getString("writer_id"),resultSet.getString("nickname"), resultSet.getString("content"), fileList,resultSet.getString("written_date"), resultSet.getInt("num_of_likes"), resultSet.getInt("retweet_num"));
                    postList.add(post);
                }
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return postList;
        }

        @Override
        protected void onPostExecute(ArrayList<Post> result) {
            if (result != null) {
                homePostRVAdapter.addPosts(result);
            }

            this.cancel(true);
        }
    }

    // 게시물 좋아요 JDBC
    @SuppressLint("StaticFieldLeak")
    public class LikePostAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... integers) {

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                int post_id = integers[0];
                // 기존에 좋아요 했는지 확인
                Statement stmt = connection.createStatement();
                String check = "select * from post_like where user_id = \""+ "yusin" +"\" and target_id = \""+ post_id +"\"";
                ResultSet rs = stmt.executeQuery(check);

                if(rs.next()){ // 좋아요 O, 좋아요 취소
                    String sql1 = "update post set num_of_likes = num_of_likes - 1 where post_id = \""+ post_id +"\"";
                    String sql2 = "delete from post_like where target_id = \""+ post_id  +"\" and user_id = \""+ "yusin" +"\"";

                    PreparedStatement pstm = connection.prepareStatement(sql1);
                    pstm.executeUpdate();
                    pstm.executeUpdate(sql2);
                }else{         // 좋아요 X, 좋아요 추가
                    String sql1 = "update post set num_of_likes = num_of_likes + 1 where post_id = \""+ post_id +"\"";
                    String sql2 = "insert into post_like values(\""+ "yusin" +"\", \""+ post_id +"\")";

                    PreparedStatement pstm = connection.prepareStatement(sql1);
                    pstm.executeUpdate();
                    pstm.executeUpdate(sql2);
                }

                return true;
            } catch (Exception e) {
                Log.e("WriteCommentAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) { // 포스트 새로고침
                new GetPostAsyncTask().execute();
            }

            this.cancel(true);
        }
    }
}
