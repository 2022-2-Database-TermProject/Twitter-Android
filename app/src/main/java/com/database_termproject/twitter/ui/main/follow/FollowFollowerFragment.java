package com.database_termproject.twitter.ui.main.follow;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;
import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.getUserId;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.database_termproject.twitter.data.User;
import com.database_termproject.twitter.databinding.FragmentFollowBinding;
import com.database_termproject.twitter.databinding.FragmentFollowFollowerBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.adapter.FollowRVAdapter;
import com.database_termproject.twitter.ui.main.search.SearchFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class FollowFollowerFragment extends BaseFragment<FragmentFollowFollowerBinding> {
    FollowRVAdapter followRVAdapter;

    @Override
    protected FragmentFollowFollowerBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentFollowFollowerBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        followRVAdapter = new FollowRVAdapter(requireContext());
        binding.followFollowerRv.setAdapter(followRVAdapter);
        followRVAdapter.setMyClickListener(new FollowRVAdapter.MyItemClickListener() {
            @Override
            public void onUnWait(@NonNull User user) {
                new UnWaitAsyncTask().execute(""+user.user_id);
            }

            @Override
            public void onUnfollow(@NonNull User user) {
                new UnFollowAsyncTask().execute(""+user.user_id);
            }

            @Override
            public void onFollow(@NonNull User user) {
                new FollowAsyncTask().execute(""+user.user_id);
            }

            @Override
            public void onClick(@NonNull User user) {
                // TODO: 유저 페이지로 이동
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        new GetFollowerAsyncTask().execute();
    }

    // 팔로워 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetFollowerAsyncTask extends AsyncTask<Void, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            ArrayList<User> userList = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select follower_id from follower where user_id = \"" + getUserId() + "\"";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while(resultSet.next()){
                    String follower_id = resultSet.getString("follower_id");
                    String sql2 = "select * from user where id = \"" + follower_id + "\"";

                    Statement statement2 = connection.createStatement();
                    ResultSet resultSet2 = statement2.executeQuery(sql2);

                    User user = null;
                    if(resultSet2.next()){
                        String nickname = resultSet2.getString("nickname"), image = resultSet2.getString("image");
                        user = new User(follower_id, nickname, image);
                    }

                    // 팔로잉 상태 확인 - 기본(0), 팔로잉(1), 대기 중(2
                    String sql3 = "select * from following where user_id = \"" + getUserId() + "\" and following_id = \"" + follower_id + "\"";
                    Statement statement3 = connection.createStatement();
                    ResultSet resultSet3 = statement3.executeQuery(sql3);

                    if(resultSet3.next()){
                        user.setFollowing(1);
                    }else{ // 팔로우 하지 않는 경우, 대기 중인지 확인
                        String sql4 = "select * from wait_follow where user_id = \"" + follower_id + "\" and follower_id = \"" + getUserId() + "\"";
                        Statement statement4 = connection.createStatement();
                        ResultSet resultSet4 = statement4.executeQuery(sql4);

                        if(resultSet4.next()){
                            user.setFollowing(2);
                        }
                    }

                    userList.add(user);
                }

            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return userList;
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            if (result != null) {
                followRVAdapter.updateUser(result);

                if(result.isEmpty()) binding.followFollowerNoTv.setVisibility(View.VISIBLE);
                else binding.followFollowerNoTv.setVisibility(View.GONE);
            }

            this.cancel(true);
        }
    }

    // 팔로우 JDBC
    @SuppressLint("StaticFieldLeak")
    public class FollowAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String user_id = getUserId();
            String follow_id = strings[0];

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                // 상대가 공개 계정인지, 비공개 계정인지 확인
                String sql1 = "select private from user where id = \"" + follow_id + "\"";
                Statement stmt1 = connection.createStatement();
                ResultSet rs1 =  stmt1.executeQuery(sql1);

                if(rs1.next()){
                    boolean isPrivate = rs1.getBoolean("private");

                    if(isPrivate){ // 비공개 계정인 경우, 팔로우 대기
                        String sql2 = "insert into wait_follow (user_id, follower_id) values (\"" + follow_id + "\", \"" + user_id + "\")";
                        PreparedStatement pstm = connection.prepareStatement(sql2);
                        pstm.executeUpdate();
                    }else{         // 공개 계정인 경우, 팔로우
                        String sql2 = "insert into follower values (\"" + follow_id + "\", \"" + user_id + "\")";
                        PreparedStatement pstm = connection.prepareStatement(sql2);
                        pstm.executeUpdate();

                        String sql3 = "insert into following values (\"" + user_id + "\", \"" + follow_id + "\")";
                        pstm.executeUpdate(sql3);
                    }

                    return true;
                }

            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // 다시 조회
                new GetFollowerAsyncTask().execute();
            }

            this.cancel(true);
        }
    }


    // 언팔로우 JDBC
    @SuppressLint("StaticFieldLeak")
    public class UnFollowAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String user_id = getUserId();
            String follow_id = strings[0];

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql1 = "delete from follower where user_id = \"" + follow_id + "\" and follower_id = \"" + user_id + "\"";
                PreparedStatement pstm = connection.prepareStatement(sql1);
                pstm.executeUpdate();

                String sql2 = "delete from following where user_id = \"" + user_id + "\" and following_id = \"" + follow_id + "\"";
                pstm.executeUpdate(sql2);

                return true;
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // 다시 조회
                new GetFollowerAsyncTask().execute();
            }

            this.cancel(true);
        }
    }

    // 대기 취소 JDBC
    @SuppressLint("StaticFieldLeak")
    public class UnWaitAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String user_id = getUserId();
            String follow_id = strings[0];

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql1 = "delete from wait_follow where user_id = \"" + follow_id + "\" and follower_id = \"" + user_id + "\"";
                PreparedStatement pstm = connection.prepareStatement(sql1);
                pstm.executeUpdate();

                return true;
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                // 다시 조회
              new GetFollowerAsyncTask().execute();
            }

            this.cancel(true);
        }
    }
}
