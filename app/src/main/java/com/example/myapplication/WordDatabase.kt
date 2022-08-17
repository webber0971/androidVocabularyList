package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

//singleton
@Database(entities = [Word::class], version = 3, exportSchema = false)
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
                    "Eeko").addMigrations(MIGRATION2_3).build()
                INSTANCE = instance
            }
            return instance
            }
        }

        var MIGRATION1_2:Migration = object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Word ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 1")
            }
        }

        var MIGRATION2_3:Migration = object:Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Word ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 0")
            }
        }

//        刪除不要的column
//        var MIGRATION_2_3 : Migration = object :Migration(2,3){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE word_temp (id INTEGER PRIMARY KEY NOT NULL,English TEXT,"+"Chinese_Meaning)")
//                database.execSQL("INSERT INTO word_temp(id,english,chinese_meaning)"+"SELECT id,english,chinese_meaning FROM word")
//                database.execSQL("ALTER TABLE word_temp RENAME TO word")
//            }
//        }



    }
    abstract fun getWordDao():WordDao
}