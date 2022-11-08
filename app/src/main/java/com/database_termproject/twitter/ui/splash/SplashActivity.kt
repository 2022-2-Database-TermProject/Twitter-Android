package com.database_termproject.twitter.ui.splash

import android.os.Handler
import com.database_termproject.twitter.databinding.ActivitySplashBinding
import com.database_termproject.twitter.ui.BaseActivity
import com.database_termproject.twitter.ui.main.ExampleActivity
import com.database_termproject.twitter.ui.main.MainActivity


class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    override fun initAfterBinding() {
        // 1초 후, Main Activity로 이동
        Handler().postDelayed(Runnable {
            startNextActivity(ExampleActivity::class.java)
            finish()
        }, 1000)
    }
}