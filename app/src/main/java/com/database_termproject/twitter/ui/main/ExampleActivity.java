package com.database_termproject.twitter.ui.main;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.database_termproject.twitter.databinding.ActivityExampleBinding;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class ExampleActivity extends AppCompatActivity {
    private static final String URL = "jdbc:mysql://192.168.35.115/mydb";
    private static final String USER = "root";
    private static final String PASSWORD = "021019@wa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityExampleBinding binding;

        super.onCreate(savedInstanceState);
        binding = ActivityExampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Button
        binding.exampleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new InfoAsyncTask().execute();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            Map<String, String> info = new HashMap<>();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT * from user";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Log.d("Example", resultSet.getString("user_id").toString());
                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return info;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (!result.isEmpty()) {
                Log.d("Example", result.toString());
            }
        }
    }
}
