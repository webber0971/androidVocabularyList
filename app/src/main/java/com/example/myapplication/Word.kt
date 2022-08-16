package com.example.myapplication

import android.telephony.mbms.StreamingServiceInfo
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Word(
    @ColumnInfo(name = "English")
    val english : String,
    @ColumnInfo(name = "Chinese_Meaning")
    val chineseMeaning : String
) {
    @PrimaryKey(autoGenerate = true)
    val id :Int=0
}