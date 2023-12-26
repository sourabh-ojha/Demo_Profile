package com.example.demo_profile.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PrefManager(private val context: Activity) {

    private fun getPref() : SharedPreferences {
        return context.getSharedPreferences("Login_Data", Context.MODE_PRIVATE)
    }

    fun save(key: String, value: String) {
        getPref().edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String? = null): String? {
        return getPref().getString(key, defValue)
    }
}