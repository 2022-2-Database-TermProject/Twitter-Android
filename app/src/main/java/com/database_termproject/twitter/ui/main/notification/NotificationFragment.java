package com.database_termproject.twitter.ui.main.notification;

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

import com.database_termproject.twitter.data.Post;
import com.database_termproject.twitter.data.User;
import com.database_termproject.twitter.databinding.FragmentNotificationBinding;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.ui.adapter.NotificationRVAdapter;
import com.database_termproject.twitter.ui.edit.EditActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NotificationFragment extends BaseFragment<FragmentNotificationBinding> {
    NotificationRVAdapter notificationRVAdapter;

    @Override
    protected FragmentNotificationBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentNotificationBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        binding.notificationMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), EditActivity.class));
            }
        });

        notificationRVAdapter = new NotificationRVAdapter(requireContext());
        binding.notificationRv.setAdapter(notificationRVAdapter);
        notificationRVAdapter.setMyClickListener(new NotificationRVAdapter.MyItemClickListener() {
            @Override
            public void onConfirm(@NonNull User user) {
                new ConfirmWaitAsyncTask().execute(""+user.user_id);
            }

            @Override
            public void onReject(@NonNull User user) {
                new RejectWaitAsyncTask().execute(""+user.user_id);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        new GetWaitAsyncTask().execute();
    }

    // 대기 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetWaitAsyncTask extends AsyncTask<Void, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            ArrayList<User> userList = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select * from wait_follow, user where user_id = \"" + getUserId() + "\" and wait_follow.follower_id = user.id";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while(resultSet.next()){
                    String id = resultSet.getString("follower_id"), nickname = resultSet.getString("nickname"), image = resultSet.getString("image");
                    User user = new User(id, nickname, image);
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
                notificationRVAdapter.updateUser(result);
            }

            this.cancel(true);
        }
    }

    // 팔로우 승인 JDBC
    @SuppressLint("StaticFieldLeak")
    public class ConfirmWaitAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String user_id = getUserId();
            String follower_id = strings[0];

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "insert into follower values (\"" + user_id + "\", \"" + follower_id + "\")";
                PreparedStatement pstm = connection.prepareStatement(sql);
                pstm.executeUpdate();

                String sql2 = "insert into following values (\"" + follower_id + "\", \"" + user_id + "\")";
                pstm.executeUpdate(sql2);

                String sql3 = "delete from wait_follow where user_id = \"" + user_id + "\" and follower_id = \"" + follower_id + "\"";
                pstm.executeUpdate(sql3);

                return true;
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                new GetWaitAsyncTask().execute();
            }

            this.cancel(true);
        }
    }

    // 팔로우 거절 JDBC
    @SuppressLint("StaticFieldLeak")
    public class RejectWaitAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            String user_id = getUserId();
            String follower_id = strings[0];

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "delete from wait_follow where user_id = \"" + user_id + "\" and follower_id = \"" + follower_id + "\"";
                PreparedStatement pstm = connection.prepareStatement(sql);
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
                new GetWaitAsyncTask().execute();
            }

            this.cancel(true);
        }
    }

}
