package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room



class MainActivity : AppCompatActivity() {
    private lateinit var insertButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cleanButton: Button
    private lateinit var updateButton: Button
    private lateinit var wordDao: WordDao
    private lateinit var wordDatabase: WordDatabase
    private lateinit var textView: TextView
    private lateinit var allWordsLive:LiveData<List<Word>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertButton = findViewById(R.id.insertButton)
        deleteButton = findViewById(R.id.deleteButton)
        cleanButton = findViewById(R.id.cleanButton)
        updateButton = findViewById(R.id.updateButton)
        wordDatabase = Room.databaseBuilder(this, WordDatabase::class.java, "Eeko").allowMainThreadQueries().build()
        wordDao = wordDatabase.getWordDao()
        allWordsLive=wordDao.getAllWordsLive()
        textView = findViewById(R.id.textView)
        allWordsLive.observe(this, Observer {
            val list: List<Word> =it
            var text: String = ""
            for (i in list.indices) {
                val tempWord = list[i]
                text = text + tempWord.id + ":" + tempWord.english + "=" + tempWord.chineseMeaning + "\n"
            }
            textView.text = text
        })

        insertButton.setOnClickListener() {
            val word1 = Word("how", "如何~")
            val word2 = Word("hello", "你好")
            val word3 = Word("taiwan", "台灣")
            wordDao.insertWords(word1,word2,word3)
        }
        deleteButton.setOnClickListener(){
            val lastWord= allWordsLive.value?.get(0)
            if (lastWord != null) {
                wordDao.deleteWords(lastWord)
            }
        }
        cleanButton.setOnClickListener(){
            wordDao.deleteAllWords()
        }
        updateButton.setOnClickListener(){
            val lastWord=  allWordsLive.value?.get(0)
            if (lastWord != null) {
                lastWord.english="404"
            }
            if (lastWord != null) {
                lastWord.chineseMeaning="404"
            }
            if (lastWord != null) {
                wordDao.updateWords(lastWord)
            }
        }

    }



}