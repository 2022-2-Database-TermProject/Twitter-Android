package com.database_termproject.twitter.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.database_termproject.twitter.utils.GlobalApplication.Companion.mSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/* user_id */
fun saveUserId(userId: String) {
    val editor = mSharedPreferences.edit()
    editor.putString("user_id", userId)

    editor.apply()
}

fun getUserId(): String? = mSharedPreferences.getString("user_id", null)

fun removeUserId(){
    val editor = mSharedPreferences.edit()

    editor.remove("user_id")
    editor.apply()
}
