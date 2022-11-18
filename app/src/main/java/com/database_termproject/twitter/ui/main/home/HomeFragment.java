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
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.database_termproject.twitter.data.Post;
import com.database_termproject.twitter.ui.BaseFragment;
import com.database_termproject.twitter.databinding.FragmentHomeBinding;
import com.database_termproject.twitter.ui.main.search.SearchBeforeFragmentDirections;
import com.database_termproject.twitter.ui.post.PostActivity;
import com.google.gson.Gson;

import org.checkerframework.checker.units.qual.A;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    HomePostRVAdapter homePostRVAdapter;

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

        homePostRVAdapter = new HomePostRVAdapter(requireContext());
        binding.homePostRv.setAdapter(homePostRVAdapter);
        homePostRVAdapter.setMyClickListener(new HomePostRVAdapter.MyItemClickListener() {
            @Override
            public void onClick(@NonNull Post post) {
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
}
