package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//singleton
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDatabase: RoomDatabase() {
    companion object{
        private var INSTANCE:WordDatabase ?= null
        @Synchronized
        fun  getDatabase(context: Context):WordDatabase {
            synchronized(this) {
            var instance = INSTANCE
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    WordDatabase::class.java,
                    "Eeko").fallbackToDestructiveMigration().build()
                INSTANCE = instance
            }
            return instance
            }
        }



    }
    abstract fun getWordDao():WordDao
}