package com.database_termproject.twitter.ui.post;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.database_termproject.twitter.R;
import com.database_termproject.twitter.databinding.ActivityPostBinding;
import com.database_termproject.twitter.ui.BaseActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;

public class PostActivity extends BaseActivity<ActivityPostBinding> {
    int REQUEST_CODE = 999;

    FirebaseStorage storage;
    StorageReference storageRef;
    ArrayList<Uri> uriList = new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();

    String userId = "yusin";

    @Override
    protected ActivityPostBinding getBinding() {
        return ActivityPostBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        setMyClickListener();
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
                // 이미지 업로드, 내용 validation
                // 이미지 업로드 끝나면, JDBC 쿼리문 날리기 insert into post ~~, file ~~
            }
        });

        binding.newpostAlbumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
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
                    Log.d("single choice: ", String.valueOf(data.getData()));
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);

                } else {      // 이미지를 여러장 선택한 경우
                    ClipData clipData = data.getClipData();
                    Log.d("clipData", String.valueOf(clipData.getItemCount()));

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();
                        uriList.add(imageUri);
                        Log.d("clipData", String.valueOf(clipData.getItemAt(i)));
                    }
                }
            }
        }
    }

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
                        }
                    }
                }
            });

        }
    }


}
