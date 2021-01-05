package com.my.mythings2.m.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author 文琳
 * @time 2020/6/22 10:00
 * @desc 物品们
 */
@Parcelize
data class Things(var list: List<Thing>?) : Parcelable