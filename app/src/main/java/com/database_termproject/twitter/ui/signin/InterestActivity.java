package com.database_termproject.twitter.ui.signin;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;
import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.getUserId;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.ui.main.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class InterestActivity extends AppCompatActivity {

    ArrayList<String> interests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        TextView cancelText = findViewById(R.id.cancel_button);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), SignUp1Activity.class);
                startActivity(myIntent);
            }
        });

        CheckBox checkBox1 = findViewById(R.id.cb_select_sports);
        CheckBox checkBox2 = findViewById(R.id.cb_select_game);
        CheckBox checkBox3 = findViewById(R.id.cb_select_study);
        CheckBox checkBox4 = findViewById(R.id.cb_select_entertainment);
        CheckBox checkBox5 = findViewById(R.id.cb_select_food);

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    checkBox1.setTextColor(Color.WHITE);
                } else {
                    checkBox1.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    checkBox2.setTextColor(Color.WHITE);
                } else {
                    checkBox2.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    checkBox3.setTextColor(Color.WHITE);
                } else {
                    checkBox3.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    checkBox4.setTextColor(Color.WHITE);
                } else {
                    checkBox4.setTextColor(Color.BLACK);
                }

            }
        });

        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    checkBox5.setTextColor(Color.WHITE);
                } else {
                    checkBox5.setTextColor(Color.BLACK);
                }

            }
        });


        Button createAccout = findViewById(R.id.finishbutton);
        createAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox1.isChecked()) interests.add("Sports");
                if (checkBox2.isChecked()) interests.add("Game");
                if (checkBox3.isChecked()) interests.add("Study");
                if (checkBox4.isChecked()) interests.add("Entertainment");
                if (checkBox5.isChecked()) interests.add("Food");

                new UploadImagesAsyncTask().execute();
            }
        });
    }

    // 관심사 추가 JDBC
    // Post 등록 JDBC
    @SuppressLint("StaticFieldLeak")
    public class UploadImagesAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

                for (String interest : interests) {
                    String query = "insert into user_has_interest (user_id, interest_id) values (\"" + getUserId() + "\", \"" + interest + "\")";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.executeUpdate();
                }

                return true;
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information" + e);
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);

                finish();
            }
            this.cancel(true);
        }
    }
}