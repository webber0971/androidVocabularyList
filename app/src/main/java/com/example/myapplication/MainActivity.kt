package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var insertButton: Button
    private lateinit var deleteButton: Button
    private lateinit var cleanButton: Button
    private lateinit var updateButton: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insertButton=findViewById(R.id.insertButton)
        deleteButton=findViewById(R.id.deleteButton)
        cleanButton=findViewById(R.id.cleanButton)
        updateButton=findViewById(R.id.updateButton)


    }

}