package com.database_termproject.twitter.ui.signin;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;
import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.saveUserId;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.ui.main.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FindPassWordActivity extends AppCompatActivity {

    EditText editTextID;
    EditText editTextEmail;

    Button nextButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        TextView cancelText = findViewById(R.id.cancel_button);
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(myIntent);
            }
        });

        nextButton = findViewById(R.id.find_password_btn);

        editTextEmail = findViewById(R.id.find_pw_email);
        editTextID = findViewById(R.id.find_pw_ID);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FindPWAsyncTask().execute();

                Log.d("ID", editTextID.getText().toString());
                Log.d("email", editTextEmail.getText().toString());
            }
        });
    }
    public class FindPWAsyncTask extends AsyncTask<Void, Void, String> {
        String result;

        @Override
        protected String doInBackground(Void... voids) {
            Statement stmt = null;
            ResultSet rs = null;
            PreparedStatement pstm = null;
            int mode;
            if (editTextEmail.getText().toString().startsWith("010")) {
                mode = 0;
            } else {
                mode = 1;
            }
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                if (mode == 0) {
                    String phonenum = editTextEmail.getText().toString();
                    String user_id = editTextID.getText().toString();
                    stmt = connection.createStatement();
                    String s1 = "select pwd from user where id = \"" + user_id + "\" and phone_num = \"" + phonenum + "\"";
                    rs = stmt.executeQuery(s1);
                    if (rs.next()) {
                        String pwd = rs.getString("pwd");
                        result = ("Password for " + user_id + ": " + pwd);
                    } else {
                        String s2 = "select phone_num from user where phone_num = \"" + phonenum + "\"";
                        ResultSet rs1 = stmt.executeQuery(s2);
                        if (rs1.next()) {
                            result = "Wrong Id";
                        } else {
                            String s3 = "select id from user where id = \"" + user_id + "\"";
                            ResultSet rs2 = stmt.executeQuery(s3);
                            if (rs2.next()) {
                                result = "Wrong phone number";
                            } else {
                                result = "Wrong Id and phone number";
                            }
                        }

                        pstm = connection.prepareStatement(s1);
                        pstm.executeUpdate();

                        result ="Find PassWord complete";
                    }


                } else if (mode == 1) {
                    String email = editTextEmail.getText().toString();
                    String user_id = editTextID.getText().toString();
                    stmt = connection.createStatement();
                    String s1 = "select pwd from user where id = \"" + user_id + "\" and email = \"" + email + "\"";
                    rs = stmt.executeQuery(s1);
                    if (rs.next()) {
                        String pwd = rs.getString("pwd");
                        result = ("Password for " + user_id + " : " + pwd);
                    } else {
                        String s2 = "select email from user where email = \"" + email + "\"";
                        ResultSet rs1 = stmt.executeQuery(s2);
                        if (rs1.next()) {
                            result = "Wrong Id";
                        } else {
                            String s3 = "select id from user where id = \"" + user_id + "\"";
                            ResultSet rs2 = stmt.executeQuery(s3);
                            if (rs2.next()) {
                                result = "Wrong email";
                            } else {
                                result = "Wrong Id and email";
                            }
                        }

                        pstm = connection.prepareStatement(s1);
                        pstm.executeUpdate();

                        result ="Find PassWord complete";
                    }

                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            this.cancel(true);

            String[] array = result.split(" ");


            if (!array[0].equals("Password")) {

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                Log.d("success?", "wrong");
            }
            else {

                Toast.makeText(getApplicationContext(), "PW : " + array[4], Toast.LENGTH_LONG).show();

                Intent myIntent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(myIntent);
            }

        }
    }

}
