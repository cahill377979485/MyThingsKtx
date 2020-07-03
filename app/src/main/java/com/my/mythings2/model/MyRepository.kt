package com.my.mythings2.model

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.my.mythings2.model.bean.Thing
import com.my.mythings2.model.bean.Things
import com.my.mythings2.xutil.MyUtil
import java.util.ArrayList

private const val DATA: String = "DATA"

/**
 * @author 文琳
 * @time 2020/6/23 11:23
 * @desc 资料库，用于MVVM作为Model来提供数据，只在ViewModel中使用，不在View中使用，换句话说，不要在Activity或者Fragment中创建此实例
 */
class MyRepository {

    val thingList: ArrayList<Thing>?
        get() {
            var list: ArrayList<Thing> = ArrayList()
            val thingsStr: String = SPUtils.getInstance().getString(DATA)
            if (thingsStr.isNotEmpty()) {
                val things: Things? = Gson().fromJson(thingsStr, Things::class.java)
                things?.let {
                    list = things.list as ArrayList<Thing>
                }
            }
            return list
        }

    /**
     * 保存到本地缓存中
     */
    fun save(list: List<Thing>?) {
        SPUtils.getInstance().put(DATA, Gson().toJson(Things(list), Things::class.java))
    }

    /**
     * 添加数据并保存
     */
    fun add(thing: Thing) {
        thingList?.let {
            it.add(0, thing)
            save(it)
        }
    }

    fun delete(name: String) {
        thingList?.let {
            for (i in it.indices) {
                val t: Thing = it[i]
                if (t.name == name) {
                    it.remove(t)
                    break
                }
            }
            save(it)
        }
    }

    fun update(updatePosition: Int, str: String) {
        thingList?.let {
            val arr: Array<String> = MyUtil.getNameAndPriceArrayByRegex(str)
            //因为是倒序所以这里要list.size-1-position
            it[it.size - 1 - updatePosition].name = arr[0]
            it[it.size - 1 - updatePosition].price = arr[1]
            save(it)
        }
    }
}