package com.database_termproject.twitter.ui.signin;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.ui.main.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SigninActivity extends AppCompatActivity {

    EditText editTextID;
    EditText editTextPW;

    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        Button createAccout = findViewById(R.id.buttonSignUp);
        createAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), SignUp1Activity.class);
                startActivity(myIntent);
            }
        });

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SigninAsyncTask().execute();

                Log.d("ID", editTextID.getText().toString());
                Log.d("PW", editTextPW.getText().toString());
            }
        });

    }

    public class SigninAsyncTask extends AsyncTask<Void, Void, String> {
        String result;

        @Override
        protected String doInBackground(Void... voids) {
            String userId = editTextID.getText().toString();
            String password = editTextPW.getText().toString();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String query = "select id from user where id = \"" + userId + "\"";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    result = "Already exist!";
                }else{


                    PreparedStatement pstm = connection.prepareStatement(query);
                    pstm.executeUpdate();

                    result ="Sign up complete";
                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            this.cancel(true);

            Intent myIntent = new Intent(getApplicationContext(),InterestActivity.class);
            startActivity(myIntent);

        }
    }
}
