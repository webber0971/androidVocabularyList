package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(isCard:Boolean): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNumber=itemView.findViewById<TextView>(R.id.textViewNumber)
        val textEnglish = itemView.findViewById<TextView>(R.id.textViewEnglish)
        val textChineseMeaning = itemView.findViewById<TextView>(R.id.textViewChinese)
    }
    var allWords : List<Word> = listOf()
    var isCard:Boolean=isCard

    //當創建viewholder時要從layout中加載view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        if(isCard){
            val itemView : View = layoutInflater.inflate(R.layout.cell_card,parent,false)
            return MyViewHolder(itemView)
        }else{
            val itemView : View = layoutInflater.inflate(R.layout.cell_normal,parent,false)
            return MyViewHolder(itemView)
        }
    }

    //綁定邏輯上的關聯，顯示textview中的內容
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word = allWords[position]
        holder.textViewNumber.text=(position+1).toString()
        holder.textEnglish.text=word.english
        holder.textChineseMeaning.text=word.chineseMeaning
        holder.itemView.setOnClickListener(){
            val uri:Uri=Uri.parse("https://translate.google.com.tw/?hl=zh-TW&sl=en&tl=zh-TW&text="+holder.textEnglish.text+"&op=translate")
            val intent :Intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
       }
    }

    override fun getItemCount(): Int {
        return allWords.size
    }

}