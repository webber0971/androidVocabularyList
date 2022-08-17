package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var insertButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cleanButton: Button
    private lateinit var updateButton: Button
    private lateinit var wordDao: WordDao
    private lateinit var wordDatabase: WordDatabase
    private lateinit var textView: TextView
    private lateinit var allWordsLive:LiveData<List<Word>>
    private lateinit var wordViewModel: WordViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertButton = findViewById(R.id.insertButton)
        deleteButton = findViewById(R.id.deleteButton)
        cleanButton = findViewById(R.id.cleanButton)
        updateButton = findViewById(R.id.updateButton)
        wordViewModel=ViewModelProvider(this).get(WordViewModel::class.java)
        allWordsLive=wordViewModel.allWordsLive
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
            wordViewModel.insertWord()
        }
        deleteButton.setOnClickListener(){
            wordViewModel.deleteWords()
        }
        cleanButton.setOnClickListener(){
            wordViewModel.deleteAllWords()
        }
        updateButton.setOnClickListener(){
            wordViewModel.updateWords()
        }
    }




}