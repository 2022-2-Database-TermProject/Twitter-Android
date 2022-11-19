package com.database_termproject.twitter.ui.main.search;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

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
import java.util.ArrayList;

public class SearchAfterFragment extends BaseFragment<FragmentSearchAfterBinding> {
    FollowRVAdapter followRVAdapter;

    @Override
    protected FragmentSearchAfterBinding getBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentSearchAfterBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initAfterBinding() {
        String args = SearchAfterFragmentArgs.fromBundle(getArguments()).getSearchWord();
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
        followRVAdapter = new FollowRVAdapter();
        binding.searchAfterRv.setAdapter(followRVAdapter);
        followRVAdapter.setMyClickListener(new FollowRVAdapter.MyItemClickListener() {
            @Override
            public void onClick(@NonNull User user) {
                // TODO: 유저 프로필 페이지로 이동
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
                String sql = "select * from user where id like '%" + searchWord + "%'";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    User user = new User(resultSet.getString("id"), resultSet.getString("nickname"), resultSet.getString("image"));
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
}
