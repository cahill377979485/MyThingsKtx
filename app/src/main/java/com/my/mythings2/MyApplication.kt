package com.my.mythings2

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils

/**
 * @author 文琳
 * @time 2020/6/22 9:38
 * @desc
 */
class MyApplication : Application() {
    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        Utils.init(this)
    }
}