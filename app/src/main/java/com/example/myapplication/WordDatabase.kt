package com.example.myapplication

import androidx.room.Database

@Database(entities = [Word::class], version = 1, exportSchema = false
)
abstract class WordDatabase {
    abstract fun getWordDao():WordDao
}