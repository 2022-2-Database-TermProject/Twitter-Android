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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.database_termproject.twitter.databinding.ActivityEditBinding;
import com.database_termproject.twitter.ui.BaseActivity;
import com.database_termproject.twitter.ui.adapter.InterestRVAdapter;
import com.database_termproject.twitter.ui.post.PostActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EditActivity extends BaseActivity<ActivityEditBinding> {
    final static int REQUEST_CODE = 1111;

    FirebaseStorage storage;
    StorageReference storageRef;

    String nickname = "";
    String region_id = "";
    boolean account_private;
    String image = "";
    Uri newImageUri = null;

    ArrayList<String> interests = new ArrayList<>();
    ArrayList<String> newInterests = new ArrayList<>();

    InterestRVAdapter interestRVAdapter;
    @Override
    protected ActivityEditBinding getBinding() {
        return ActivityEditBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        setMyClickListener();
        interestRVAdapter = new InterestRVAdapter(this);
        binding.editInterestRv.setAdapter(interestRVAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 사용자 정보 조회 JDBC
        new GetUserAsyncTask().execute();
    }

    private void setMyClickListener() {
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
                nickname = binding.editNicknameEt.getText().toString();
                account_private = binding.editPublicSb.isChecked();
                newInterests = interestRVAdapter.getInterestList();

                if(newImageUri != null){
                    uploadImages(newImageUri);
                }else{
                    new UpdateUserAsyncTask().execute();
                }
            }
        });
    }

    private void goToGallery() {
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
                newImageUri = null;
            } else {// 이미지를 하나라도 선택한 경우
                newImageUri = (Uri) data.getData();
            }
        }
    }

    // Firebase에 이미지 올리기
    String user_id = "yusin";

    private void uploadImages(Uri uri) {
        String fileName = "profile/" + user_id + "_" + System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUri = task.getResult().toString();
                    image = downloadUri;
                    Log.d("Firebase", downloadUri);

                    new UpdateUserAsyncTask().execute();
                }
            }
        });
    }


    //// ------------
    // 사용자 정보 조회 JDBC
    @SuppressLint("StaticFieldLeak")
    public class GetUserAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            String user_id = "yusin";

            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "select id, nickname, region_id, private, image from user where id =  \"" + user_id + "\"";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    nickname = resultSet.getString("nickname");
                    region_id = resultSet.getString("region_id");
                    account_private = resultSet.getBoolean("private");
                    image = resultSet.getString("image");

                    String sql2 = "select interest_id from user_has_interest where user_id = \"" + user_id + "\"";
                    Statement statement2 = connection.createStatement();
                    ResultSet resultSet2 = statement2.executeQuery(sql2);

                    while (resultSet2.next()) {
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

                Log.d("Image", image + "<-");
                if(image!= null){
                    Glide.with(binding.getRoot())
                            .load(image)
                            .apply(new RequestOptions().circleCrop())
                            .into(binding.editProfileIv);
                }

                // 관심사
                interestRVAdapter.addInterestList(interests);
                newInterests = interests; // 초기 상태 저장
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

            // 유저 정보 update
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String sql = "update user set nickname = \"" + nickname + "\", " +
                        " region_id = \"" + region_id + "\", " +
                        "private = " + account_private +
                        ", image = \"" + image + "\"" +
                        " where id = \"" + user_id + "\"";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();

                String last = "";
                for(String interest: interests){
                    last += interest + ", ";
                }

                String after = "";
                for(String interest: newInterests){
                    after += interest + ", ";
                }

                Log.d("Edit-기존", last);
                Log.d("Edit-다음", after);

                // 관심사 정보 update
                for(String newInterest: newInterests){
                    if(!interests.contains(newInterest)){ // select 추가
                        Log.d("Edit-추가", newInterest);
                        String sql2 = "insert into user_has_interest (user_id, interest_id) values (\"" + user_id + "\", \"" + newInterest + "\")";
                        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                        preparedStatement2.executeUpdate();
                    }
                }

                for(String interest: interests){
                    if(!newInterests.contains(interest)){ // select 삭제
                        Log.d("Edit-삭제", interest);
                        String sql2 = "delete from user_has_interest where user_id = \"" + user_id + "\" and interest_id = \"" + interest + "\"";
                        PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                        preparedStatement2.executeUpdate();
                    }
                }

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
