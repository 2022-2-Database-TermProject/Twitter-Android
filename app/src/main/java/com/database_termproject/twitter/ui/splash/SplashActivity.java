package com.database_termproject.twitter.ui.splash;

import static com.database_termproject.twitter.utils.SharedPreferenceManagerKt.getUserId;

import com.database_termproject.twitter.databinding.ActivitySplashBinding;
import com.database_termproject.twitter.ui.BaseActivity;
import com.database_termproject.twitter.ui.example.ExampleActivity;
import com.database_termproject.twitter.ui.main.MainActivity;
import com.database_termproject.twitter.ui.signin.SigninActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    @Override
    protected ActivitySplashBinding getBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initAfterBinding() {
        // 1초 후, 이동
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(getUserId()!=null)
                    startNextActivity(MainActivity.class);
                else
                    startNextActivity(SigninActivity.class);


                finish();
            }
        }, 1000);
    }
}
