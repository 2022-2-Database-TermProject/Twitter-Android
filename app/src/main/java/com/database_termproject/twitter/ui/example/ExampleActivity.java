package com.database_termproject.twitter.ui.example;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import com.database_termproject.twitter.databinding.ActivityExampleBinding;
import com.database_termproject.twitter.ui.BaseActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class ExampleActivity extends BaseActivity<ActivityExampleBinding> {
    @Override
    protected ActivityExampleBinding getBinding() {
        return ActivityExampleBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
       // 유저 조회
        binding.exampleGetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new GetUserAsyncTask().execute();
            }
        });

        // 회원가입
        binding.exampleSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SignupAsyncTask().execute();
            }
        });

        // 로그인
        binding.exampleSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SigninAsyncTask().execute();
            }
        });
    }

    // 유저 조회
    @SuppressLint("StaticFieldLeak")
    public class GetUserAsyncTask extends AsyncTask<Void, Void, Map<String, String>> {
        @Override
        protected Map<String, String> doInBackground(Void... voids) {
            Map<String, String> info = new HashMap<>();
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "SELECT * from user";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    info.put(resultSet.getString("user_id"), resultSet.getString("password"));
                }
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return info;
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (!result.isEmpty()) {
                Log.d("Example", result.toString());
                binding.exampleGetTv.setText(result.toString());

                this.cancel(true);
            }
        }
    }

    // 회원가입
    @SuppressLint("StaticFieldLeak")
    public class SignupAsyncTask extends AsyncTask<Void, Void, String> {
        String result;

        @Override
        protected String doInBackground(Void... voids) {
            String userId = binding.exampleIdEt.getText().toString();
            String password = binding.examplePwEt.getText().toString();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String query = "SELECT user_id from user where user_id='" + userId + "'";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    result = "Already exist!";
                }else{
                    // Email, phone
                    String email = "hello@gmail.com";
                    String phone = "010-0000-0000";

                    // Current date
                    LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));

                    query = "insert into user (user_id, password, email, phone_num, registered_on, public) values ('"+userId + "', '" + password + "', '"
                            + email + "', '"+
                            phone +"', '" + now + "', 1)";

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
            binding.exampleGetTv.setText(result);
            this.cancel(true);
        }
    }

    // 로그인
    @SuppressLint("StaticFieldLeak")
    public class SigninAsyncTask extends AsyncTask<Void, Void, String> {
        String result;

        @Override
        protected String doInBackground(Void... voids) {
            String userId = binding.exampleIdEt.getText().toString();
            String password = binding.examplePwEt.getText().toString();

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String query = "select user_id from user where user_id = \"" + userId + "\" and password=\"" + password + "\"";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                if (rs.next()) {
                    result = "Login Success";
                }else{
                    result = "There's no such a user";
                }
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            binding.exampleGetTv.setText(result);
            this.cancel(true);
        }
    }
}
