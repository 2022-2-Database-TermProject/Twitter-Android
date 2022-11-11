package com.database_termproject.twitter.ui.post;

import android.view.View;

import com.database_termproject.twitter.databinding.ActivityPostBinding;
import com.database_termproject.twitter.ui.BaseActivity;

public class PostActivity extends BaseActivity<ActivityPostBinding> {
    @Override
    protected ActivityPostBinding getBinding() {
        return ActivityPostBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        setMyClickListener();
    }

    private void setMyClickListener(){
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

            }
        });
    }
}
