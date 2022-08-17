package com.example.myapplication

import android.telephony.mbms.StreamingServiceInfo
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Word(
    @ColumnInfo(name = "English")
    var english : String,
    @ColumnInfo(name = "Chinese_Meaning")
    var chineseMeaning : String
) {
    @PrimaryKey(autoGenerate = true)
    var id :Int=0
    @ColumnInfo(name = "bar_data")
    var barData:Boolean=false
    @ColumnInfo(name = "chinese_invisible")
    var chineseInvisible:Boolean=false
}