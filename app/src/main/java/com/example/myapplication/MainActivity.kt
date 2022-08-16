package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.room.Room



class MainActivity : AppCompatActivity() {
    private lateinit var insertButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cleanButton: Button
    private lateinit var updateButton: Button
    private lateinit var wordDao: WordDao
    private lateinit var wordDatabase: WordDatabase
    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertButton = findViewById(R.id.insertButton)
        deleteButton = findViewById(R.id.deleteButton)
        cleanButton = findViewById(R.id.cleanButton)
        updateButton = findViewById(R.id.updateButton)
        wordDatabase = Room.databaseBuilder(this, WordDatabase::class.java, "Eeko").allowMainThreadQueries().build()
        wordDao = wordDatabase.getWordDao()
        textView = findViewById(R.id.textView)

        updateView()
        insertButton.setOnClickListener() {
            val word1 = Word("how", "如何~")
            val word2 = Word("hello", "你好")
            val word3 = Word("taiwan", "台灣")
            wordDao.insertWords(word1,word2,word3)
            updateView()
        }
        deleteButton.setOnClickListener(){
            val lastWord=wordDao.getAllWords()[0]
            wordDao.deleteWords(lastWord)
            updateView()
        }
        cleanButton.setOnClickListener(){
            wordDao.deleteAllWords()
            updateView()
        }
        updateButton.setOnClickListener(){
            val lastWord=wordDao.getAllWords()[0]
            lastWord.english="404"
            lastWord.chineseMeaning="404"
            wordDao.updateWords(lastWord)
            updateView()
        }

    }

    private fun updateView() {
        val list: List<Word> = wordDao.getAllWords()
        var text: String = ""
        for (i in list.indices) {
            val tempWord = list[i]
            text = text + tempWord.id + ":" + tempWord.english + "=" + tempWord.chineseMeaning + "\n"
        }
        textView.text = text
    }

}