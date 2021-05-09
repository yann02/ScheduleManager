package com.shkj.cm.modules.detail.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class ScheduleEntity(
    var createtime: String?,
    var dtag: Int,
    var endTime: String?,
    var freq: Int,
    var gmt: String?,
    var monoId: String?,
    var startTime: String?,
    var tid: String?,
    var title: String?,
    var preTime: List<String>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createtime)
        parcel.writeInt(dtag)
        parcel.writeString(endTime)
        parcel.writeInt(freq)
        parcel.writeString(gmt)
        parcel.writeString(monoId)
        parcel.writeString(startTime)
        parcel.writeString(tid)
        parcel.writeString(title)
        parcel.writeStringList(preTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScheduleEntity> {
        override fun createFromParcel(parcel: Parcel): ScheduleEntity {
            return ScheduleEntity(parcel)
        }

        override fun newArray(size: Int): Array<ScheduleEntity?> {
            return arrayOfNulls(size)
        }
    }

}