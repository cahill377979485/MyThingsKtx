package com.my.mythings2.v.binder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.my.mythings2.databinding.ThingBinding
import com.my.mythings2.m.bean.Thing
import com.my.mythings2.m.bean.TypeData
import com.my.mythings2.xutil.InAnimation
import com.my.mythings2.xutil.TypeDataNames
import org.greenrobot.eventbus.EventBus

/**
 * @author 文琳
 * @time 2020/6/16 17:27
 * @desc 物品的样式
 */
class ThingBinder : ItemViewBinder<Thing, ThingBinder.ThingHolder>() {
    private val mAnimation = InAnimation()

    inner class ThingHolder(var binding: ThingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ThingHolder {
        return ThingHolder(ThingBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ThingHolder, item: Thing) {
        holder.binding.item = item
        holder.binding.click = object : ClickThing {
            override fun onClick(thing: Thing) {
                EventBus.getDefault().post(TypeData(TypeDataNames.clickThing, thing))
            }
        }
        holder.binding.executePendingBindings()
        for (anim in mAnimation.getAnimators(holder.itemView)) {
            anim.interpolator = DecelerateInterpolator()
            anim.setDuration(300).start()
        }
    }
}