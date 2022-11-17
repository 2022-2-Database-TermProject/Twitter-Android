package com.database_termproject.twitter.ui.post;

import static com.database_termproject.twitter.utils.GlobalApplication.PASSWORD;
import static com.database_termproject.twitter.utils.GlobalApplication.URL;
import static com.database_termproject.twitter.utils.GlobalApplication.USER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.database_termproject.twitter.databinding.ActivityPostBinding;
import com.database_termproject.twitter.ui.BaseActivity;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;

public class PostActivity extends BaseActivity<ActivityPostBinding> {
    int REQUEST_CODE = 999;

    FirebaseStorage storage;
    StorageReference storageRef;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();

    String userId = "yusin";
    String content = "";

    PostAlbumRVAdapter postAlbumRVAdapter = null;

    @Override
    protected ActivityPostBinding getBinding() {
        return ActivityPostBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setMyClickListener();
        initRV();
    }

    private void setMyClickListener() {
        // 취소 버튼 클릭 시,
        binding.newpostCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 트윗 버튼 클릭 시,
        binding.newpostTweetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 포스트 업로드
                content = binding.newpostContentEt.getText().toString();
                new UploadPostAsyncTask().execute();
            }
        });

        binding.newpostAlbumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void initRV(){
        postAlbumRVAdapter = new PostAlbumRVAdapter(this);
        postAlbumRVAdapter.setMyClickListener(new PostAlbumRVAdapter.MyItemClickListener(){

            @Override
            public void onDeleted(@NonNull ArrayList<Uri> list) {
                uriList.clear();
                uriList.addAll(list);
            }
        });
        binding.newpostAlbumRv.setAdapter(postAlbumRVAdapter);
    }

    /* 권한 가져오기 */
    private void checkPermission() {
        int readCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        } else {
            goToGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (Arrays.stream(grantResults).allMatch(new IntPredicate() {
                @Override
                public boolean test(int i) {
                    return i == PackageManager.PERMISSION_GRANTED;
                }
            })) {   // GRANTED
                goToGallery();
            } else { // DENIED
                showToast("권한을 허용해야 사진을 가져올 수 있습니다. ");
            }
        }
    }

    // 앨범에서 이미지 가져오기
    private void goToGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data == null) { // 하나도 선택하지 않은 경우,
                showToast("이미지를 선택하지 않았습니다.");
            } else {// 이미지를 하나라도 선택한 경우
                if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);

                } else {      // 이미지를 여러장 선택한 경우
                    ClipData clipData = data.getClipData();

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        uriList.add(imageUri);
                    }
                }


                postAlbumRVAdapter.addImages(uriList);
            }
        }
    }

    String post_id;
    // Firebase에 이미지 올리기
    private void uploadImages(){
        for(Uri uri: uriList){
            String fileName = "img/" + userId + "_" + System.currentTimeMillis() + ".jpg";
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
                        imgList.add(downloadUri);

                        if(uri == uriList.get(uriList.size() - 1)){ // 모든 이미지 업로드 완료
                            Log.d("Firebase", imgList.toString());
                            // 이미지 업로드 쿼리문 실행
                            new UploadImagesAsyncTask().execute();
                        }
                    }
                }
            });

        }
    }

    // Post 등록 JDBC
    @SuppressLint("StaticFieldLeak")
    public class UploadPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

                LocalDateTime now = LocalDateTime.now();
                String createAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);

                String query = "insert into post (writer_id, content, written_date) values ('" + userId + "','" + content + "','" + createAt + "');";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.executeUpdate();

                Statement stmt = connection.createStatement();
                String s1 = "select post_id from post where writer_id = \"" + userId + "\" and written_date=\"" + createAt + "\"";
                ResultSet rs = stmt.executeQuery(s1);
                if(rs.next()){
                    post_id =  rs.getString("post_id");
                }

                Log.d("Post", "Post is uploaded");
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information"+ e);
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                this.cancel(true);

                if(uriList.isEmpty()){ // 이미지 없는 경우,
                    finish();
                }else{
                    uploadImages();
                }
            }
        }
    }

    // Post 등록 JDBC
    @SuppressLint("StaticFieldLeak")
    public class UploadImagesAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

                for(String img: imgList){
                    String query = "insert into file (post_id, file) values ('" + post_id + "','" + img + "');";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    preparedStatement.executeUpdate();
                }

                Log.d("Post", "Post is uploaded");
            } catch (Exception e) {
                Log.e("GetUserAsyncTask", "Error reading school information"+ e);
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                this.cancel(true);


                finish();
            }
        }
    }
}
