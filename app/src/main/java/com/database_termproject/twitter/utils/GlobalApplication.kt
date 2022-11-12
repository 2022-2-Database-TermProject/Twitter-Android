package com.database_termproject.twitter.utils

import com.database_termproject.twitter.BuildConfig

class GlobalApplication {
    companion object{
        const val USER = BuildConfig.USER;          // user 이름
        const val PASSWORD = BuildConfig.PASSWORD; // password
        const val URL = BuildConfig.URL; // url
    }
}