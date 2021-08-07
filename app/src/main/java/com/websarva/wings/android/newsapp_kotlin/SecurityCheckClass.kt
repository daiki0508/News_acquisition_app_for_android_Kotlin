package com.websarva.wings.android.newsapp_kotlin

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug
import java.io.File

class SecurityCheckClass(){
    companion object{
        init {
            System.loadLibrary("main")
        }
    }

    fun rootCheck(): Boolean{
        for (pathDir in System.getenv("PATH")!!.split(":")){
            if (File(pathDir, "su").exists()){
                return true
            }
        }
        return false
    }

    fun checkRunningProcess(activity: Activity): Boolean{
        val manager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = manager.getRunningServices(300) as List<ActivityManager.RunningServiceInfo>?

        if (list != null){
            for (i in list.indices){
                val tempName = list[i].process
                if (tempName.contains("supersu") || tempName.contains("superuser")){
                    return true
                }
            }
        }
        return false
    }

    fun isDebuggable(context: Context): Boolean{
        return ((context.applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0)
    }

    fun detectDebugger(): Boolean{
        return Debug.isDebuggerConnected()
    }

    fun isTestKeyBuild(): Boolean{
        val str = Build.TAGS
        if ((str != null) && (str.contains("test-keys")))
            return true

        return false
    }

    external fun crashOnInit()
    external fun antidebug()
}