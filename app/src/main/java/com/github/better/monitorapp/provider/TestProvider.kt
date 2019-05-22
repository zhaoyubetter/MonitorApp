package com.github.better.monitorapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

/**
 * @author zhaoyu1  2019/5/17
 **/
class TestProvider : ContentProvider() {

    companion object {
        const val Authority = "com.github.better.monitorapp.provider.TestProvider"
    }

    // Provider onCreate 比 Application onCreate 方法还要早执行
    override fun onCreate(): Boolean {
        Log.e("better", "TestProvider --> onCreate")
        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        return Uri.parse(Authority)
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val cursor = MatrixCursor(arrayOf("better"))
        cursor.addRow(arrayOf("better"))
        return cursor
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri?): String {
        return "0"
    }

}