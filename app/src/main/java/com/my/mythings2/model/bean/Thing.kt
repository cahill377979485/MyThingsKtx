package com.my.mythings2.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author 文琳
 * @time 2020/6/22 9:57
 * @desc 物品
 */
@Parcelize
data class Thing(var position: Int, var name: String, var price: String) : Parcelable