package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var insertButton: Button
    private lateinit var cleanButton: Button
    private lateinit var allWordsLive:LiveData<List<Word>>
    private lateinit var wordViewModel: WordViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter_normal: MyAdapter
    private lateinit var myAdapter_card: MyAdapter
    private lateinit var switch: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertButton = findViewById(R.id.insertButton)
        cleanButton = findViewById(R.id.cleanButton)
        switch=findViewById(R.id.switchInActivity)
        wordViewModel=ViewModelProvider(this).get(WordViewModel::class.java)
        allWordsLive=wordViewModel.allWordsLive
        //創建recycleview實體
        recyclerView=findViewById(R.id.recyclerView)
        myAdapter_normal= MyAdapter(false,wordViewModel)
        myAdapter_card= MyAdapter(true,wordViewModel)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=myAdapter_normal
        switch.setOnClickListener(){
            if(switch.isChecked){
                recyclerView.adapter=myAdapter_card
            }else{
                recyclerView.adapter=myAdapter_normal
            }
        }
        allWordsLive.observe(this, Observer {
            val tempListSize=myAdapter_normal.allWords.size
            myAdapter_card.allWords = it
            myAdapter_normal.allWords = it
            if(tempListSize != it.size) {
                myAdapter_normal.notifyDataSetChanged()
                myAdapter_card.notifyDataSetChanged()
            }
        })
        insertButton.setOnClickListener() {
            wordViewModel.insertWord()
        }
        cleanButton.setOnClickListener(){
            wordViewModel.deleteAllWords()
        }
    }




}