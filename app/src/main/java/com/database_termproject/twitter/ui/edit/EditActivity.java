package com.database_termproject.twitter.ui.edit;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import com.database_termproject.twitter.databinding.ActivityEditBinding;
import com.database_termproject.twitter.ui.BaseActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EditActivity extends BaseActivity<ActivityEditBinding> {
    final static int REQUEST_CODE = 1111;

    String nickname = "";
    String region_id = "";
    boolean account_private;
    ArrayList<String> interests = new ArrayList<>();

    @Override
    protected ActivityEditBinding getBinding() {
        return ActivityEditBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        setMyClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 사용자 정보 조회 JDBC
        new GetUserAsyncTask().execute();
    }

    private void setMyClickListener(){
        binding.editCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.editNewProfileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGallery();
            }
        });

        binding.editSaveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname= binding.editNicknameEt.getText().toString();
                account_private = binding.editPublicSb.isChecked();

                // 사용자 정보 수정 JDBC
                new UpdateUserAsyncTask().execute();
            }
        });
    }

    private void goToGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) { // 하나도 선택하지 않은 경우,
                showToast("이미지를 선택하지 않았습니다.");
            } else {// 이미지를 하나라도 선택한 경우
                Uri uri = (Uri) data.getData();
            }
        }
    }

    //// ------------
    // 사용자 정보 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetUserAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            String user_id = "yusin";

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select id, nickname, region_id, private from user where id =  \"" + user_id + "\"";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if(resultSet.next()){
                    nickname = resultSet.getString("nickname");
                    region_id = resultSet.getString("region_id");
                    account_private = resultSet.getBoolean("private");

                    String sql2 = "select interest_id from user_has_interest where user_id = \"" + user_id + "\"";
                    Statement statement2 = connection.createStatement();
                    ResultSet resultSet2 = statement2.executeQuery(sql2);

                    while (resultSet2.next()){
                        interests.add(resultSet2.getString("interest_id"));
                    }


                    return true;
                }
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                binding.editNicknameEt.setText(nickname);
                binding.editRegionRegionEt.setText(region_id);
                binding.editPublicSb.setChecked(account_private);
            }

            this.cancel(true);
        }
    }

    // 사용자 정보 수정 JDBC
    @SuppressLint("StaticFieldLeak")
    public class UpdateUserAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            String user_id = "yusin";

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "update user set nickname = \"" + nickname + "\", " +
                        " region_id = \"" + region_id + "\", " +
                        "private = " + account_private +
                        " where id = \"" + user_id + "\"";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();

                return true;

            } catch (Exception e) {
                Log.e("UpdateUserAsyncTask", "Error reading school information", e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                finish();
            }

            this.cancel(true);
        }
    }
}
