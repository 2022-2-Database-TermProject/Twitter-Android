package com.database_termproject.twitter.utils

import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlin.math.roundToInt


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

fun getDeviceWidth(): Int {
    // px 반환
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getDeviceHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun convertDpToPx(context: Context, dp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density).roundToInt()
}

fun convertPxToDp(context: Context, px: Int): Int {
    val density = context.resources.displayMetrics.density
    return (px / density).roundToInt()
}

fun convertDpToSp(context: Context, dp: Int): Int {
    return (convertDpToPx(context, dp) / context.resources.displayMetrics.scaledDensity).toInt()
}

// 절대 경로 가져오기
fun getRealPath(context: Context, uri: Uri): String? {
    val proj = arrayOf(MediaStore.Images.Media.DATA)
    val c: Cursor? = context.contentResolver.query(uri, proj, null, null, null)
    val index: Int = c!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    c.moveToFirst()
    return c.getString(index)
}