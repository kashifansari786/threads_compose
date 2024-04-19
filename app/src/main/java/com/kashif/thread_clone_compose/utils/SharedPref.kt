package com.kashif.thread_clone_compose.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Mohammad Kashif Ansari on 19,April,2024
 */
object SharedPref {

    fun storeData( email: String="",
                    name: String="",
                    bio: String="",
                    userName: String="",
                    imageUrl: String="",
                   uid:String="",
                    context:Context){
        val sharedPref:SharedPreferences=context.getSharedPreferences("users", Context.MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.putString("name",name)
        editor.putString("email",email)
        editor.putString("bio",bio)
        editor.putString("userName",userName)
        editor.putString("imageUrl",imageUrl)
        editor.putString("uid",uid)
        editor.apply()
    }

    fun getUserName(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",Context.MODE_PRIVATE)
        return sharedPreferences.getString("userName","")!!
    }
    fun getName(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",Context.MODE_PRIVATE)
        return sharedPreferences.getString("name","")!!
    }
    fun getUserEmail(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",Context.MODE_PRIVATE)
        return sharedPreferences.getString("email","")!!
    }
    fun getUserBio(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",Context.MODE_PRIVATE)
        return sharedPreferences.getString("bio","")!!
    }
    fun getUserImage(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",Context.MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl","")!!
    }
    fun getUserUid(context: Context):String{
        val sharedPreferences=context.getSharedPreferences("users",Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid","")!!
    }
}