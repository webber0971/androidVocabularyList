package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(isCard:Boolean,wordViewModel: WordViewModel):
    ListAdapter<Word, MyAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<Word>(){
        //使用ListAdapter可以更方便的使用其對於列表更新時的動畫，使畫面變動時更平順

        //先比較列表中的兩個元素是否相同(primaryKey是否相同)
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id        }
        //在比較內容是否相同
        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.english.equals(newItem.english) && oldItem.chineseMeaning.equals(newItem.chineseMeaning) && oldItem.chineseInvisible==newItem.chineseInvisible
        }

    }) {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNumber=itemView.findViewById<TextView>(R.id.textViewNumber)
        val textViewEnglish = itemView.findViewById<TextView>(R.id.textViewEnglish)
        val textViewChineseMeaning = itemView.findViewById<TextView>(R.id.textViewChinese)
        val switchChineseInvisible=itemView.findViewById<Switch>(R.id.switchChineseInvisible)
    }
//    var allWords : List<Word> = listOf()  用listAdapter時有自帶列表，使用getItem即可
    var isCard:Boolean=isCard
    val wordViewModel=wordViewModel

    //當創建viewholder時要從layout中加載view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        var itemView:View=layoutInflater.inflate(R.layout.cell_card,parent,false)
        if(isCard){
            itemView = layoutInflater.inflate(R.layout.cell_card,parent,false)
        }else{
            itemView = layoutInflater.inflate(R.layout.cell_normal,parent,false)
        }
        val holder:MyViewHolder= MyViewHolder(itemView)
        holder.itemView.setOnClickListener() {
            val uri: Uri =
                Uri.parse("https://translate.google.com.tw/?hl=zh-TW&sl=en&tl=zh-TW&text=" + holder.textViewEnglish.text + "&op=translate")
            val intent: Intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
        }
        holder.switchChineseInvisible.setOnClickListener(){
            val word:Word=holder.itemView.getTag(R.id.word_for_view_holder) as Word
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

        return MyViewHolder(itemView)

    }
    //當出現在螢幕上時就顯示序列號
    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.textViewNumber.text=(holder.adapterPosition+1).toString()
    }

    //綁定邏輯上的關聯，顯示textview中的內容
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word = getItem(position)
        holder.itemView.setTag(R.id.word_for_view_holder,word)
        holder.textViewNumber.text=(position+1).toString()
        holder.textViewEnglish.text=word.english
        holder.textViewChineseMeaning.text=word.chineseMeaning
        if(word.chineseInvisible){
            holder.textViewChineseMeaning.visibility=View.GONE
            holder.switchChineseInvisible.isChecked=true
        }else{
            holder.textViewChineseMeaning.visibility=View.VISIBLE
            holder.switchChineseInvisible.isChecked=false
        }
    }

//    override fun getItemCount(): Int {
//        return itemCount
//    }

}