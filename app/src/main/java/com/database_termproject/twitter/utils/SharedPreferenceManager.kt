package com.database_termproject.twitter.utils

import com.database_termproject.twitter.utils.GlobalApplication.Companion.mSharedPreferences

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