package com.database_termproject.twitter.ui.main.search;

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

import com.database_termproject.twitter.data.Post;
import com.database_termproject.twitter.data.User;
import com.database_termproject.twitter.databinding.FragmentSearchAfterBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.adapter.FollowRVAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchAfterFragment extends BaseFragment<FragmentSearchAfterBinding> {
    String args;
    FollowRVAdapter followRVAdapter;

    @Override
    protected FragmentSearchAfterBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSearchAfterBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        args = SearchAfterFragmentArgs.fromBundle(getArguments()).getSearchWord();
        binding.searchAfterTv.setText(args);

        setMyClickListener();
        initRVAdapter();

        new GetUserAsyncTask().execute(args);
    }

    private void setMyClickListener(){
        binding.searchAfterBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController().popBackStack();
            }
        });

        binding.searchAfterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findNavController().popBackStack();
            }
        });
    }

    private void initRVAdapter(){
        followRVAdapter = new FollowRVAdapter(requireContext());
        binding.searchAfterRv.setAdapter(followRVAdapter);
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

    // 유저 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetUserAsyncTask extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... strings) {
            ArrayList<User> userList = new ArrayList<>();
            String searchWord = strings[0];

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select * from user where id like '%" + searchWord + "%' and id != \"" + getUserId() + "\"";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String id = resultSet.getString("id"), nickname = resultSet.getString("nickname"), image = resultSet.getString("image");
                    User user = new User(id, nickname, image);

                    // 팔로잉 상태 확인 - 기본(0), 팔로잉(1), 대기 중(2
                    String query2 = "select * from following where user_id = \"" + getUserId() + "\" and following_id = \"" + id + "\"";
                    Statement statement2 = connection.createStatement();
                    ResultSet resultSet2 = statement2.executeQuery(query2);

                    if(resultSet2.next()){
                        user.setFollowing(1);
                    }else{ // 팔로우 하지 않는 경우, 대기 중인지 확인
                        String query3 = "select * from wait_follow where user_id = \"" + id + "\" and follower_id = \"" + getUserId() + "\"";
                        Statement statement3 = connection.createStatement();
                        ResultSet resultSet3 = statement3.executeQuery(query3);

                        if(resultSet3.next()){
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

               if(result.isEmpty())
                   binding.searchAfterNoResultTv.setVisibility(View.VISIBLE);
               else
                   binding.searchAfterNoResultTv.setVisibility(View.GONE);
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
                new GetUserAsyncTask().execute(args);
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
                new GetUserAsyncTask().execute(args);
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
                new GetUserAsyncTask().execute(args);
            }

            this.cancel(true);
        }
    }
}
