package com.example.data.storage

import android.content.Context

class AppPreferencesImpl(private val context: Context): AppPreferences {

    private val sharedPreferences = context.getSharedPreferences(NOW_PREF, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()


    fun userHasLoggedIn() {
        putBoolean(IS_USER_LOGGED_IN, true)
    }

    fun userHasNotLoggedIn() {
        putBoolean(IS_USER_LOGGED_IN, false)
    }


    fun isUserLoggedIn(): Boolean {
        return getBoolean(IS_USER_LOGGED_IN, false)
    }


    @Synchronized private fun putLong(key: String, value:Long) {
        editor.putLong(key,value)
        editor.commit()
    }

    @Synchronized private fun getLong(key: String, default: Long): Long {
        return sharedPreferences.getLong(key, default)
    }

    @Synchronized private fun putInt(key: String, value:Int) {
        editor.putInt(key,value)
        editor.commit()
    }

    @Synchronized private fun getInt(key: String, default: Int): Int {
        return sharedPreferences.getInt(key, default)
    }

    @Synchronized private fun putFloat(key: String, value:Float) {
        editor.putFloat(key,value)
        editor.commit()
    }

    @Synchronized private fun getFloat(key: String, default: Float): Float {
        return sharedPreferences.getFloat(key, default)
    }

    @Synchronized private fun putBoolean(key: String, boolVal: Boolean) {
        editor.putBoolean(key,boolVal)
        editor.commit()
    }

    @Synchronized private fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    companion object {
        private val NOW_PREF = "com.example.amazonbillingexampl.nowpreferences"
        private val IS_USER_LOGGED_IN = "com.example.amazonbillingexampl.IS_USER_LOGGED_IN"
    }
}