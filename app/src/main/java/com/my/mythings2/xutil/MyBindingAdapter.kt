package com.my.mythings2.xutil

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * @author 文琳
 * @time 2020/7/2 10:52
 * @desc 用于MVVM架构在xml文件中直接使用指定的key="value"来设置和绑定数据
 */
object MyBindingAdapter {

    @JvmStatic//不加这个注解会报错说没有静态方法
    @BindingAdapter(value = ["app:smoothScrollToTopFlag"])
    fun smoothScrollToPosition(rv: RecyclerView, smoothScrollToTopFlag: Boolean = false) {
        if (smoothScrollToTopFlag) rv.smoothScrollToPosition(0)
    }

    @JvmStatic
    @BindingAdapter(value = ["app:refreshFlag", "app:itemViewCacheSize"])
    fun setItemViewCacheSize(rv: RecyclerView, refreshFlag: Boolean = false, size: Int = 5) {
        if (refreshFlag) rv.setItemViewCacheSize(size)
    }

    @JvmStatic
    @BindingAdapter(value = ["app:addTextChangedListener"])
    fun addTextChangedListener(et: EditText, watcher: TextWatcher) {
        et.addTextChangedListener(watcher)
    }

//    @JvmStatic
//    @BindingAdapter(value = ["app:gridLayoutManager"])
//    fun setLayoutManager(rv:RecyclerView, span:Int){
//        rv.apply {
//            layoutManager = GridLayoutManager()
//        }
//    }
}