package com.example.grocery.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.grocery.models.User

class SessionManager(var mContext: Context){
    val KEY_EMAIL = "email"
    val KEY_NAME = "name"
    val KEY_MOBILE = "mobile"
    val FILE_NAME = "my_file"
    val IS_LOGGED_IN = "logged in"
    val KEY_ID = "id"
    val KEY_PASSWORD = "password"
    val KEY_TOTAL = "total"
    val KEY_DISCOUNT = "discount"

    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor

    init{
        sharedPreferences =mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

    }
    fun saveData(user: User) {
        editor.putString(KEY_NAME, user.firstName)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_MOBILE, user.mobile)
        editor.putString(KEY_ID, user._id)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.commit()
    }

    fun login():Boolean{
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun logout(){
        mContext.deleteDatabase(DBHelper.FILE_NAME)
        editor.clear()
        editor.commit()
    }

    fun getUser(KEY:String):String?{
        return sharedPreferences.getString(KEY, null)
    }

    fun user():User{

        var _id= sharedPreferences.getString(KEY_ID,null).toString()
        var email= sharedPreferences.getString(KEY_EMAIL, null).toString()
        var mobile = sharedPreferences.getString(KEY_MOBILE, null).toString()

        return User(_id = _id, email = email, mobile = mobile)
    }



}