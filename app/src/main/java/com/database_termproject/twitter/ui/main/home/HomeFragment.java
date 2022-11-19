package com.database_termproject.twitter.ui.main.home;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;
import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.getUserId;

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
import com.database_termproject.twitter.ui.adapter.PostImageRVAdapter;
import com.database_termproject.twitter.ui.adapter.PostRVAdapter;
import com.database_termproject.twitter.ui.dialog.DeleteDialogFragment;
import com.database_termproject.twitter.ui.dialog.RetweetDialogFragment;
import com.database_termproject.twitter.ui.post.PostActivity;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

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
        new GetRecommendPostAsyncTask().execute();
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
            public void delete(@NonNull Post post) { // 더보기 클릭 시, 포스트 삭제 모달 띄우기
                showDeleteDialog(post);
            }

            @Override
            public void like(@NonNull Post post) { // 좋아요 클릭 시, 게시물 좋아요 JDBC
                new LikePostAsyncTask().execute(post.post_id);
            }

            @Override
            public void retweet(@NonNull Post post) { // 리트윗 클릭 시, 리트윗 모달 띄우기
                showRetweetDialog(post);
            }

            @Override
            public void onClick(@NonNull Post post) { // 클릭 시, 포스트 상세 보기로 이동
                String postJson = new Gson().toJson(post);
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToPostDetailFragment(postJson);
                findNavController().navigate(action);
            }
        });
    }

    private void showRetweetDialog(Post post){
        RetweetDialogFragment retweetDialogFragment = new RetweetDialogFragment();
        retweetDialogFragment.show(getFragmentManager(), null);
        retweetDialogFragment.setMyDialogCallback(new RetweetDialogFragment.MyDialogCallback() {
            @Override
            public void confirm(@NonNull String content) { // 리트윗 JDBC
                String post_id = "" + post.post_id;
                new WriteRetweetAsyncTask().execute(post_id, content);
            }
        });
    }

    private void showDeleteDialog(Post post){
        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
        deleteDialogFragment.show(getFragmentManager(), null);
        deleteDialogFragment.setMyDialogCallback(new DeleteDialogFragment.MyDialogCallback() {
            @Override
            public void delete() { // 포스트 삭제 JDBC
                new DeletePostAsyncTask().execute(post.post_id);
            }
        });
    }

    // ------

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

                    Post post = new Post(resultSet.getInt("post_id"), resultSet.getString("writer_id"),resultSet.getString("nickname"), resultSet.getString("content"), fileList,resultSet.getString("written_date"), resultSet.getInt("num_of_likes"), resultSet.getInt("retweet_num"), resultSet.getInt("retweet_post"));
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
                String check = "select * from post_like where user_id = \""+ getUserId() +"\" and target_id = \""+ post_id +"\"";
                ResultSet rs = stmt.executeQuery(check);

                if(rs.next()){ // 좋아요 O, 좋아요 취소
                    String sql1 = "update post set num_of_likes = num_of_likes - 1 where post_id = \""+ post_id +"\"";
                    String sql2 = "delete from post_like where target_id = \""+ post_id  +"\" and user_id = \""+ getUserId() +"\"";

                    PreparedStatement pstm = connection.prepareStatement(sql1);
                    pstm.executeUpdate();
                    pstm.executeUpdate(sql2);
                }else{         // 좋아요 X, 좋아요 추가
                    String sql1 = "update post set num_of_likes = num_of_likes + 1 where post_id = \""+ post_id +"\"";
                    String sql2 = "insert into post_like values(\""+ getUserId() +"\", \""+ post_id +"\")";

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

    // 리트윗 JDBC
    @SuppressLint("StaticFieldLeak")
    public class WriteRetweetAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String post_id = strings[0];
                String content = strings[1];
                String user_id = getUserId();

                LocalDateTime now = LocalDateTime.now();
                String createAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);

                String sql = "insert into post(content, writer_id, retweet_post, written_date) values(\"" + content + "\", \"" + user_id + "\", \"" + post_id + "\", \""+ createAt + "\")";
                String sql2 = "update post set retweet_num = retweet_num + 1 where post_id = " + post_id;

                PreparedStatement pstm = connection.prepareStatement(sql);
                pstm.executeUpdate();
                pstm.executeUpdate(sql2);

                return true;
            } catch (Exception e) {
                Log.e("WriteRetweetAsyncTask", "Error reading school information", e);
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

    // 포스트 삭제 JDBC
    @SuppressLint("StaticFieldLeak")
    public class DeletePostAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... integers) {

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                int post_id = integers[0];

                // Retweet post면, 원본 포스트의 retweet num 지우기
                Statement stmt = connection.createStatement();
                String check = "select * from post where post_id = " + post_id;
                ResultSet rs = stmt.executeQuery(check);
                if(rs.next()) {
                    String updateSql = "update post set retweet_num = retweet_num - 1 where post_id = " + rs.getInt("retweet_post");
                    PreparedStatement pstm = connection.prepareStatement(updateSql);
                    pstm.executeUpdate();
                }

                String sql = "delete from post where post_id = " + post_id;
                PreparedStatement pstm = connection.prepareStatement(sql);
                pstm.executeUpdate();

                return true;
            } catch (Exception e) {
                Log.e("WriteRetweetAsyncTask", "Error reading school information", e);
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

    // 추천 게시물 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetRecommendPostAsyncTask extends AsyncTask<Void, Void, Post> {
        @Override
        protected Post doInBackground(Void... voids) {
            String user_id = getUserId();
            String user_region = "";
            Post post;

            // TODO: 쿼리문에 문제 있음
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                HashMap<String, Integer> similarity = new HashMap<>();

                // (0) 모든 유저 hashmap에 저장
                Statement stmt0 = connection.createStatement();
                String sql0 = "select id, region_id from user";
                ResultSet rs0 = stmt0.executeQuery(sql0);

                while (rs0.next()) {
                    String id = rs0.getString("id");
                    similarity.put(id, 0);

                    if (id.equals(user_id)) {
                        user_region = rs0.getString("region_id");
                    }
                }

                // (1) 동일한 관심사를 가진 유저들 +1씩
                Statement stmt = connection.createStatement();
                String sql = "select user_id from user_has_interest "
                        + "where interest_id in (select interest_id from user_has_interest where user_id = \"" + user_id
                        + "\") " + "and user_id != \"" + user_id + "\";";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String id = rs.getString("user_id");
                    int prev = similarity.get(id);
                    similarity.replace(id, prev + 1);
                }

                // (2) 동일한 지역에 사는 유저들 +1씩
                Statement stmt2 = connection.createStatement();
                String sql2 = "select id from user where region_id = \"" + user_region + "\" and id != \"" + user_id
                        + "\";";
                ResultSet rs2 = stmt2.executeQuery(sql2);

                while (rs2.next()) {
                    String id = rs2.getString("id");
                    int prev = similarity.get(id);
                    similarity.replace(id, prev + 1);
                }

                // (3) 유사도가 가장 높은 유저 한 명 선정
                String reco_user = null;
                for (String key : similarity.keySet()) {
                    Integer value = similarity.get(key);
                    if (Objects.equals(value, Collections.max(similarity.values())))
                        reco_user = key;
                }

                // (4) 유사도가 가장 높은 유저 한 명의 포스트 중 가장 최신의 포스트 출력
                String sql3 = "select * from post, user where post.writer_id = user.id and " +
                        "writer_id = \"" + reco_user
                        + "\" order by written_date desc limit 1;";
                Statement stmt3 = connection.createStatement();
                ResultSet rs3 = stmt3.executeQuery(sql3);

                while (rs3.next()) {
                    int origin = rs3.getInt("retweet_post");
                    if (origin != 0) { // 리트윗 포스트
                        Log.d("Post", "origin " + origin);
                        String sql4 = "select * from post, user where post.writer_id = user.id and post_id = \"" + origin + "\"";
                        Statement stmt4 = connection.createStatement();
                        ResultSet rs4 = stmt4.executeQuery(sql4);

                        if(rs4.next()){
                            String writer_id = rs4.getString("writer_id");
                            String nickname = rs4.getString("nickname");
                            String content = rs4.getString("content");
                            String written_date = rs4.getString("written_date");
                            int num_of_likes = rs4.getInt("num_of_likes");
                            int retweet_num = rs4.getInt("retweet_num");

                            Log.d("Post", "retweet_num " + retweet_num);

                            // 파일 있으면 가져오기
                            sql = "select * from file where post_id = " + origin;
                            PreparedStatement statement1 = connection.prepareStatement(sql);
                            ResultSet resultSet2 = statement1.executeQuery();

                            ArrayList<String> fileList = new ArrayList<>();
                            while (resultSet2.next()) {
                                String file = resultSet2.getString("file");
                                fileList.add(file);
                            }

                            post = new Post(origin, writer_id, nickname, content, fileList, written_date, num_of_likes, retweet_num, null);
                            return  post;
                        }
                    }else{
                        int post_id = rs3.getInt("post_id");
                        String content = rs3.getString("content");
                        String nickname = rs3.getString("nickname");
                        String written_date = rs3.getString("written_date");
                        int num_of_likes = rs3.getInt("num_of_likes");
                        int retweet_num = rs3.getInt("retweet_num");

                        // 파일 있으면 가져오기
                        sql = "select * from file where post_id = " + post_id;
                        PreparedStatement statement1 = connection.prepareStatement(sql);
                        ResultSet resultSet2 = statement1.executeQuery();

                        ArrayList<String> fileList = new ArrayList<>();
                        while(resultSet2.next()){
                            String file = resultSet2.getString("file");
                            fileList.add(file);
                        }

                        post = new Post(post_id, reco_user, nickname, content, fileList, written_date, num_of_likes, retweet_num, null);
                        return post;
                    }
                }

            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Post post) {
            if (post != null) {
                Log.d("Post", "post2 - " +  post.content + "retwet - " + post.retweet_num);

                binding.itemPostProfileTv.setText(post.nickname);
                binding.itemPostUseridTv.setText("@" + post.writer_id);
                binding.itemPostContentTv.setText(post.content);

                if (post.num_of_likes > 0) binding.itemPostLikeTv.setText(""+ post.num_of_likes);
                else binding.itemPostLikeTv.setText("");
                if (post.retweet_num > 0) binding.itemPostRetweetTv.setText("" + post.retweet_num);
                else binding.itemPostRetweetTv.setText("");

                PostImageRVAdapter postImageRVAdapter = new PostImageRVAdapter(requireContext(), post.fileList);
                binding.itemPostImagesRv.setAdapter(postImageRVAdapter);

                binding.homeRecommendLayout.setVisibility(View.VISIBLE);
            }else{
                binding.homeRecommendLayout.setVisibility(View.GONE);
            }


            this.cancel(true);
        }
    }
}
