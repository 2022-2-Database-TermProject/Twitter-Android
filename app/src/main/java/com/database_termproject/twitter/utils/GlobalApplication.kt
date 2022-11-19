package com.database_termproject.twitter.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.database_termproject.twitter.BuildConfig

class GlobalApplication: Application() {
    companion object{
        const val TAG = "Twitter";
        const val USER = BuildConfig.USER;          // user 이름
        const val PASSWORD = BuildConfig.PASSWORD; // password
        const val URL = BuildConfig.URL; // url


        lateinit var mSharedPreferences: SharedPreferences         //APP 기본 SharedPreference
    }

    override fun onCreate() {
        super.onCreate()

        //앱 sharedPreference
        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
}
}