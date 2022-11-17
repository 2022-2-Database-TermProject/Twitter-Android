package com.database_termproject.twitter.ui.signin;


import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.database_termproject.twitter.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SignUp1Activity extends AppCompatActivity {

    EditText editTextNickname;
    EditText editTextID;
    EditText editTextPW;
    EditText editTextRegion;
    EditText editTextEmail;
    EditText editTextAge;

    Button nextButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        TextView cancelText = findViewById(R.id.cancel_button);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(myIntent);
            }
        });


        nextButton = findViewById(R.id.buttonNext1);

        editTextNickname = findViewById(R.id.textInputName1);
        editTextEmail = findViewById(R.id.textInputEmail1);
        editTextAge = findViewById(R.id.textInputAge1);
        editTextID = findViewById(R.id.textInputID1);
        editTextPW = findViewById(R.id.textInputPW1);
        editTextRegion = findViewById(R.id.textInputaddress1);


        TextView emailToPhone = findViewById(R.id.changePhoneNumber);
        emailToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(),SignUp2Activity.class);
                startActivity(myIntent);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SignupAsyncTask().execute();

                Log.d("nick", editTextNickname.getText().toString());
                Log.d("ID", editTextID.getText().toString());
                Log.d("PW", editTextPW.getText().toString());
                Log.d("Age", editTextAge.getText().toString());
                Log.d("email", editTextEmail.getText().toString());
            }
        });


    }

    public class SignupAsyncTask extends AsyncTask<Void, Void, String> {
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
                    // name, age, nickname, region
                    String age = editTextAge.getText().toString();
                    String nickname = editTextNickname.getText().toString();
                    String region = editTextRegion.getText().toString();
                    String email = editTextEmail.getText().toString();

                    // 이메일 넣는 경우 쿼리
                    query = "insert into user (id, pwd, age, nickname, email, region_id) values ('"+userId + "', '" + password + "', '" +age+"', '"
                            +nickname +"', '" + email + "','" + region + "')";

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
