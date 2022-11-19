package com.database_termproject.twitter.ui.signin;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;
import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.saveUserId;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        editTextID = findViewById(R.id.textInputEditTextID);
        editTextPW = findViewById(R.id.textInputEditTextPassword);

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SigninAsyncTask().execute();
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
                String query = "select id from user where id = \"" + userId + "\" and pwd=\"" + password + "\"";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    result = "Login success!" + userId;
                }else{
                    String s2 = "Select id from user where id = \""+ userId+"\"";

                    Statement stmt1 = connection.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(s2);

                    if (rs1.next()) {
                        result = "Wrong Password, Do it again";
                    }
                    else {
                        result = "Wrong ID";

                    }


                    PreparedStatement pstm = connection.prepareStatement(query);
                    pstm.executeUpdate();

                    result ="Sign in complete";
                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            this.cancel(true);

            String[] array = result.split("!");


            if (array[0].equals("Login success")) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);

                saveUserId(array[1]);
            }
            else {
                Log.d("success?", "wrong");
            }

        }
    }
}
