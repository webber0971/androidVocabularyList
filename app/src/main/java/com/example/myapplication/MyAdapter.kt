package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(isCard:Boolean,wordViewModel: WordViewModel): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNumber=itemView.findViewById<TextView>(R.id.textViewNumber)
        val textViewEnglish = itemView.findViewById<TextView>(R.id.textViewEnglish)
        val textViewChineseMeaning = itemView.findViewById<TextView>(R.id.textViewChinese)
        val switch = itemView.findViewById<Switch>(R.id.switchInActivity)
        val switchChineseInvisible=itemView.findViewById<Switch>(R.id.switchChineseInvisible)
    }
    var allWords : List<Word> = listOf()
    var isCard:Boolean=isCard
    val wordViewModel=wordViewModel

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
        holder.textViewEnglish.text=word.english
        holder.textViewChineseMeaning.text=word.chineseMeaning
        holder.switchChineseInvisible.setOnClickListener(null)
        if(word.chineseInvisible){
            holder.textViewChineseMeaning.visibility=View.GONE
            holder.switchChineseInvisible.isChecked=true
        }else{
            holder.textViewChineseMeaning.visibility=View.VISIBLE
            holder.switchChineseInvisible.isChecked=false
        }
        holder.itemView.setOnClickListener() {
            val uri: Uri =
                Uri.parse("https://translate.google.com.tw/?hl=zh-TW&sl=en&tl=zh-TW&text=" + holder.textViewEnglish.text + "&op=translate")
            val intent: Intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
        }
        holder.switchChineseInvisible.setOnClickListener(){
            if(holder.switchChineseInvisible.isChecked){
                holder.textViewChineseMeaning.visibility=View.GONE
                word.chineseInvisible=true
                wordViewModel.updateWords(word)
            }else{
                holder.textViewChineseMeaning.visibility=View.VISIBLE
                word.chineseInvisible=false
                wordViewModel.updateWords(word)
            }
        }
    }

    override fun getItemCount(): Int {
        return allWords.size
    }

}