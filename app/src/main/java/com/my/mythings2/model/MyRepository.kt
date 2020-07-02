package com.my.mythings2.model

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.my.mythings2.model.bean.Thing
import com.my.mythings2.model.bean.Things
import com.my.mythings2.util.MyUtil
import java.util.ArrayList

private const val DATA: String = "DATA"

/**
 * @author 文琳
 * @time 2020/6/23 11:23
 * @desc
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

    fun save(list: List<Thing>?) {
        SPUtils.getInstance().put(DATA, Gson().toJson(
            Things(
                list
            ), Things::class.java))
    }

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
            val arr: Array<String> = MyUtil.getNameAndPrice(str)
            //因为是倒序所以这里要list.size-1-position
            it[it.size - 1 - updatePosition].name = arr[0]
            it[it.size - 1 - updatePosition].price = arr[1]
            save(it)
        }
    }
}