package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.room.*

@Dao //data access object
interface WordDao {
    @Insert
    fun insertWords(vararg word: Word)

    @Delete
    fun deleteWords(vararg word: Word)

    @Update
    fun updateWords(vararg word: Word)

    @Query("DELETE FROM WORD")
    fun deleteAllWords()

    @Query("SELECT * FROM WORD WHERE English LIKE:patten ORDER BY ID DESC")
    fun findWordsWithPatten(patten:String):LiveData<List<Word>>

    @Query("SELECT * FROM WORD ORDER BY ID DESC")
    fun getAllWordsLive():LiveData<List<Word>>
}