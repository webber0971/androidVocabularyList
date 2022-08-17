package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel


class WordViewModel(application: Application) :AndroidViewModel(application){
    private val wordRepository=WordRepository(application)
    val allWordsLive = wordRepository.allWordsLive

    fun insertWord(vararg word: Word){
        wordRepository.insertWord(*word)
    }
    fun deleteWords(vararg word: Word){
        wordRepository.deleteWords(*word)

    }
    fun updateWords(vararg word: Word){
        wordRepository.updateWords(*word)

    }
    fun deleteAllWords(){
        wordRepository.deleteAllWords()
    }
}