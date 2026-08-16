package com.englishworld

import android.app.Application
import android.util.Log
import android.widget.Toast

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            Log.e("AppCrash", "崩溃信息", e)
            val msg = "${e.javaClass.simpleName}: ${e.message}"
            android.os.Handler(android.os.Looper.getMainLooper()).post {
                Toast.makeText(this, "错误: $msg", Toast.LENGTH_LONG).show()
            }
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}
