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
                val word1 = Word("how", "如何~")
                val word2 = Word("hello", "你好")
                val word3 = Word("taiwan", "台灣")
                wordDao.insertWords(word1, word2, word3)
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
                val allWordsSize = allWordsLive.value?.size
                if (allWordsSize != 0) {
                    val lastWord = allWordsLive.value?.get(0)
                    if (lastWord != null) {
                        lastWord.english = "404"
                    }
                    if (lastWord != null) {
                        lastWord.chineseMeaning = "404"
                    }
                    if (lastWord != null) {
                        wordDao.updateWords(lastWord)
                    }
                }
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
}