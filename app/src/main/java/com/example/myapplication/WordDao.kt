package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao //data access object
interface WordDao {
    @Insert
    fun insertWords(vararg word: Word)

    @Delete
    fun deleteWords(vararg word: Word)

    @Query("DELETE FROM WORD")
    fun deleteAllWords()

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    fun getNewWords():List<Word>
}