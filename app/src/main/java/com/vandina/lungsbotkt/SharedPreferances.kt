package com.vandina.lungsbotkt

import android.content.Context

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class SharedPreferances (val context: Context){

    private val sharedName = "shared_name"
    private val sharedPref = context.getSharedPreferences(sharedName,Context.MODE_PRIVATE)
    private val editor = sharedPref.edit()


    fun setSessionString(key: String,value: String){
        editor.putString(key,value)
        editor.commit()
    }

    fun setSessionInt(key:String,value:Int){
        editor.putInt(key,value)
        editor.commit()
    }

    fun getSessionString(key: String):String?{
        return sharedPref.getString(key,null)
    }

    fun getSessionInt(key:String):Int?{
       return  sharedPref.getInt(key,0)
    }

    fun clearSession(){
        editor.clear()
        editor.commit()
    }

    companion object{
        const val key_value = "key_level"
        const val key_namaDepan = "key_namaDepan"
        const val key_namaBelakang = "key_level"
    }

}