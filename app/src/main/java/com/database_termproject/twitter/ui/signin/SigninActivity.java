package com.database_termproject.twitter.ui.signin;

import com.database_termproject.twitter.databinding.ActivitySigninBinding;
import com.database_termproject.twitter.ui.BaseActivity;

public class SigninActivity extends BaseActivity<ActivitySigninBinding> {
    @Override
    protected ActivitySigninBinding getBinding() {
        return ActivitySigninBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {

    }
}
