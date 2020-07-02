package com.my.mythings2.viewmodel

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.my.mythings2.model.MyRepository
import com.my.mythings2.util.ToastUtils
import com.my.mythings2.bean.Thing
import com.my.mythings2.util.MyUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 文琳
 * @time 2020/6/22 10:45
 * @desc 主页面的ViewModel
 */
class MainVM(application: Application) : AndroidViewModel(application) {
    var dataList: MutableLiveData<List<Thing>> = MutableLiveData()//物品列表数据
    var totalStr: MutableLiveData<String> = MutableLiveData()//总价值统计文本
    var etText: MutableLiveData<String> = MutableLiveData()//输入框的文本
    var searchFlag: MutableLiveData<Boolean> = MutableLiveData()//搜索模式开关
    var updateFlag: MutableLiveData<Boolean> = MutableLiveData()//更新模式开关
    var refreshFlag: MutableLiveData<Boolean> = MutableLiveData()//刷新开关
    private var updatePosition: Int = 0//更新的位置
    private val repository by lazy { MyRepository() }//本地存储工具类
    val watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (searchFlag.value == true) {
                etText.value?.let {
                    if (it.isEmpty()) {
                        refreshData()
                    } else {
                        afterTextChanged(s.toString())
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    init {
        searchFlag.value = false
        updateFlag.value = false
    }

    /**
     * 从本地获取并刷新数据
     */
    fun refreshData() {
        repository.thingList?.let { updateDataAndTotalStr(it, "总价值") }
        refreshFlag.postValue(true)
    }

    private fun addThing(str: String) {
        val arr: Array<String> = MyUtil.getNameAndPrice(str)
        repository.thingList?.let {
            for (i in it.indices) {
                if (it[i].name == arr[0]) {
                    ToastUtils.instance?.apply { showError("已存在") }
                    return
                }
            }
        }
        repository.add(Thing(0, arr[0], arr[1]))
        searchFlag.postValue(false)
        etText.postValue("")
        refreshData()
    }

    fun deleteThing(name: String) {
        repository.delete(name)
        refreshData()
    }

    private fun updateThing(str: String) {
        repository.update(updatePosition, str)
        refreshData()
        updateFlag.postValue(false)
    }

    fun tryUpdateThing(thing: Thing) {
        updateFlag.postValue(true)
        etText.postValue("")
        updatePosition = thing.position
    }

    private fun updateDataAndTotalStr(list: ArrayList<Thing>, preString: String) {
        var total = 0f
        list.let {
            for (i in it.indices) {
                it[i].position = it.size - 1 - i
                val price = (it[i].price).toFloat()
                total += price.times(100)
            }
            dataList.postValue(it)//这句因为数据改变，在MainActivity中被观察到，所以会自动更新列表
        }
        val str: String = preString + "：" + total / 100
        totalStr.postValue(str.replace(".00$".toRegex(), "").replace(".0$".toRegex(), ""))
    }

    private fun afterTextChanged(s: String) {
        repository.thingList?.let {
            val listResult: ArrayList<Thing> = ArrayList()
            for (i in it.indices) {
                val t: Thing = it[i]
                if (t.name.contains(s))
                    listResult.add(t)
            }
            updateDataAndTotalStr(listResult, "总价值小计")
        }
    }

    fun clickInput(v: View) {
        etText.value?.let {
            if (it.isEmpty()) {
                ToastUtils.instance?.apply { showInfo("请先输入") }
                refreshData()
            } else {
                if (updateFlag.value == true) updateThing(it) else addThing(it)
            }
        }
    }

    fun clickCheckBox(v: View) {
        searchFlag.value?.let {
            searchFlag.postValue(!it)
        }
    }

    fun clickClear(v: View) {
        etText.postValue("")
    }
}