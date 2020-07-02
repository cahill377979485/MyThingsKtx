package com.my.mythings2.view

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.my.mythings2.*
import com.my.mythings2.model.bean.Thing
import com.my.mythings2.model.bean.TypeData
import com.my.mythings2.view.binder.ThingBinder
import com.my.mythings2.databinding.ActivityMainBinding
import com.my.mythings2.util.MyUtil
import com.my.mythings2.util.ToastUtils
import com.my.mythings2.util.TypeDataNames
import com.my.mythings2.viewmodel.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import me.drakeet.multitype.Items
import me.drakeet.multitype.MultiTypeAdapter
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 主页面
 */
class MainActivity : AppCompatActivity() {
    private lateinit var mViewModel: MainVM
    private var mItems: Items = Items()
    private lateinit var mAdapter: MultiTypeAdapter
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        observeData()
    }

    private fun init() {
        EventBus.getDefault().register(this)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(MainVM::class.java)
        //这句没加的话，xml里面设置的东西就不会生效，比如点击事件
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = this//重要！DataBinding加上这句之后，绑定了LiveData数据源的xml控件才会随着数据变化而改变。
        mAdapter = MultiTypeAdapter(mItems)
        mAdapter.register(
            Thing::class.java,
            ThingBinder()
        )
        mBinding.rv.apply {
            layoutManager = GridLayoutManager(context, 1)
            setHasFixedSize(true)
            adapter = mAdapter
        }
        MyUtil.setHelper(mBinding.rv, mItems, mAdapter)
    }

    private fun observeData() {
        mViewModel.apply {
            val owner: LifecycleOwner = this@MainActivity
            dataList.observe(owner, Observer {
                if (mItems.size > 0) {
                    mItems.clear()
                    mAdapter.notifyDataSetChanged()
                }
                mItems.addAll(it)
                mAdapter.notifyDataSetChanged()
            })
            searchFlag.observe(owner, Observer {
                if (it) {
                    val str: String = et.text.toString().trim()
                    mBinding.et.setText(str)
                    mBinding.et.setSelection(str.length)
                } else {
                    mViewModel.refreshData()
                }
            })
            refreshData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun threadHandle(typeData: TypeData) {
        when (typeData.name) {
            TypeDataNames.clickThing -> {
                val thing = typeData.data as Thing
                AlertDialog.Builder(this).apply {
                    setTitle("操作")
                    setNeutralButton("删除") { dialog, _ ->
                        mViewModel.deleteThing(thing.name)
                        dialog.dismiss()
                    }
                    setNegativeButton("更新") { dialog, _ ->
                        val str: String = thing.name + thing.price
                        mBinding.et.setText(str)
                        mBinding.et.setSelection(str.length)
                        mViewModel.tryUpdateThing(thing)
                        dialog.dismiss()
                    }
                    setPositiveButton("取消", null)
                    create()
                    show()
                }
            }
        }
    }
}