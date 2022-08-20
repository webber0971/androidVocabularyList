package com.example.myapplication

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordRepository(context: Context) {
    val wordDatabase = WordDatabase.getDatabase(context.applicationContext)
    val wordDao = wordDatabase.getWordDao()
    val allWordsLive: LiveData<List<Word>> = wordDao.getAllWordsLive()

    fun insertWord(vararg word: Word) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                wordDao.insertWords(*word)
            }
        }
    }

    fun deleteWords(vararg word: Word) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                val allWordsSize = allWordsLive.value?.size
                if (allWordsSize != 0) {
                    val lastWord = allWordsLive.value?.get(0)
                    if (lastWord != null) {
                        wordDao.deleteWords(lastWord)
                    }
                }
            }
        }
    }

    fun updateWords(vararg word: Word) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                wordDao.updateWords(*word)
            }
        }
    }

    fun deleteAllWords() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                wordDao.deleteAllWords()
            }
        }
    }
    //sqlite模糊匹配前後需加上%
    fun findWordsWithPatten(patten : String ):LiveData<List<Word>>{
        return wordDao.findWordsWithPatten("%"+patten+"%")
    }

}