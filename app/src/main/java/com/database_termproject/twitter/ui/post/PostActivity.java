package com.database_termproject.twitter.ui.post;

import com.database_termproject.twitter.databinding.ActivityPostBinding;
import com.database_termproject.twitter.ui.BaseActivity;

public class PostActivity extends BaseActivity<ActivityPostBinding> {
    @Override
    protected ActivityPostBinding getBinding() {
        return ActivityPostBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {

    }
}
