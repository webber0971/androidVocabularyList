package com.example.myapplication

import android.animation.Animator
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.SearchView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WordsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordsFragment() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var wordViewModel: WordViewModel
    private lateinit var recycleView: RecyclerView
    private lateinit var myAdapterCard: MyAdapter
    private lateinit var myAdapterNormal: MyAdapter
    private lateinit var floatButton: FloatingActionButton
    private lateinit var filterWords : LiveData<List<Word>>
    private lateinit var allWords: List<Word>
    private val VIEW_TYPE_SHP: String = "view_type_shp"
    private val IS_USING_CARD_VIEW: String = "is_using_card_view"
    private var undoAction: Boolean = false
    private lateinit var dividerItemDecoration: DividerItemDecoration //邊線

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.cleanData -> alertDialog()
            R.id.switchViewType -> switchViewType()
        }
        return super.onOptionsItemSelected(item)


    }
    fun switchViewType(){
        val shp: SharedPreferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shp.edit()
        val viewType: Boolean = shp.getBoolean(IS_USING_CARD_VIEW, false)
        if (viewType) {
            recycleView.adapter = myAdapterNormal
            recycleView.addItemDecoration(dividerItemDecoration)
            editor.putBoolean(IS_USING_CARD_VIEW, false)
        } else {
            recycleView.adapter = myAdapterCard
            recycleView.removeItemDecoration(dividerItemDecoration)
            editor.putBoolean(IS_USING_CARD_VIEW, true)
        }
        editor.apply()
    }
    fun alertDialog() {
        val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("清空數據")
        val positiveListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                wordViewModel.deleteAllWords()
            }
        }
        val negativeListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.cancel()
            }

        }
        builder.setPositiveButton("確定", positiveListener)
        builder.setNegativeButton("取消", negativeListener)
        builder.show()


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //用R.menu.menu填充到menu內
        inflater.inflate(R.menu.menu,menu)
        //取得searchView實體，限制最大寬度為1000
        val searchView :SearchView=menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.maxWidth=500
        //監聽輸入內容
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            //當監聽內容改變時要同時改變顯示的List
            override fun onQueryTextChange(newText: String?): Boolean {
                println(newText)
                val patten :String= newText.toString().trim()
                //移除onActivityCreated中的觀察，避免碰撞
                filterWords.removeObservers(viewLifecycleOwner)
                filterWords=wordViewModel.findWordsWithPatten(patten)
                filterWords.observe(viewLifecycleOwner,object :Observer<List<Word>>{
                    override fun onChanged(it: List<Word>) {
                        val temp = myAdapterNormal.itemCount
                        //滑動移除時避免空指針
                        allWords=it
                        if(temp!=it.size){
//                            myAdapterNormal.notifyDataSetChanged()
//                            myAdapterCard.notifyDataSetChanged()
                            //改成listAdapter後可使用其方法，submitList即可進行新舊list的比較
                            myAdapterNormal.submitList(it)
                            myAdapterCard.submitList(it)
                        }
                    }

                })
                return true
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //optionMenu默認為不顯示，要改成顯示才會出現
        setHasOptionsMenu(true)

        floatButton=requireActivity().findViewById(R.id.floatingActionButton)
        wordViewModel=ViewModelProvider(requireActivity()).get(WordViewModel::class.java)
        //創建邊線，並於使用myadapterNormal時使用，card本身就有不需要
        dividerItemDecoration= DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL)
        recycleView=requireActivity().findViewById(R.id.recycleView)
        recycleView.layoutManager=LinearLayoutManager(requireActivity())
        recycleView.itemAnimator= object : DefaultItemAnimator(){
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                //在動畫結束後執行，刷新序列號
                super.onAnimationFinished(viewHolder)
                val layoutManager:LinearLayoutManager= recycleView.layoutManager as LinearLayoutManager
                //找出顯示在螢幕上第一個item的position
                if(layoutManager!=null){
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    for(i in firstPosition..lastPosition){
                        val holder :MyAdapter.MyViewHolder=recycleView.findViewHolderForAdapterPosition(i) as MyAdapter.MyViewHolder
                        if(holder!=null) {
                            holder.textViewNumber.text = (i + 1).toString()
                        }
                    }
                }
            }
        }
        myAdapterCard= MyAdapter(true,wordViewModel)
        myAdapterNormal= MyAdapter(false,wordViewModel)
        val shp: SharedPreferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE)
        val viewType: Boolean = shp.getBoolean(IS_USING_CARD_VIEW, false)
        println(viewType)
        println("_________________")
        if(viewType){
            recycleView.adapter=myAdapterCard
        }else{
            recycleView.adapter=myAdapterNormal
            recycleView.addItemDecoration(dividerItemDecoration)
        }
        //初始化filterWords為不過濾，顯示所有
        filterWords=wordViewModel.allWordsLive
        filterWords.observe(viewLifecycleOwner){
            val temp = myAdapterNormal.itemCount
            //滑動移除時避免空指針
            allWords=it
            if(temp!=it.size){
//                            myAdapterNormal.notifyDataSetChanged()
//                            myAdapterCard.notifyDataSetChanged()
                //改成listAdapter後可使用其方法，submitList即可進行新舊list的比較
                myAdapterNormal.submitList(it)
                myAdapterCard.submitList(it)
                //當新增使畫面新增時滾動提醒使用者畫面有刷新，刪除與恢復刪除時不滾動
                if(temp<it.size && !undoAction){
                recycleView.smoothScrollBy(0,-200)
                }
                undoAction=false
            }
        }

        //加入滑動刪除功能，要使用recycleView的輔助工具itemTouchHelper功能
        val itemTouchHelper : ItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START or ItemTouchHelper.END){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val wordDelete :Word= allWords.get(viewHolder.adapterPosition)
                //從數據層面刪除
                wordViewModel.deleteWords(wordDelete)
                Snackbar.make(requireActivity().findViewById(R.id.frameLayoutWords),"刪除了一個詞彙",Snackbar.LENGTH_SHORT).setAction("撤銷",object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        undoAction=true
                        wordViewModel.insertWord(wordDelete)
                    }
                }).show()
            }
            val drawable: Drawable? = ContextCompat.getDrawable(requireActivity(),R.drawable.ic_baseline_delete_forever_24)
            val background : ColorDrawable = ColorDrawable(Color.LTGRAY)
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean,
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                val itemView =viewHolder.itemView
                val iconMargin :Int = (itemView.height- (drawable?.intrinsicHeight ?: 0))/2
                val iconLeft: Int
                val iconRight: Int
                val iconBottom: Int
                val backLeft: Int
                val backRight: Int
                val backTop: Int = itemView.top
                val backBottom: Int = itemView.bottom
                val iconTop: Int = itemView.top+(itemView.height- (drawable?.intrinsicHeight ?: 0))/2
                iconBottom=iconTop+ (drawable?.intrinsicHeight ?: 0)
                if(dX>0) {
                    backLeft = itemView.left
                    backRight = itemView.left + dX.toInt()
                    background.setBounds(backLeft, backTop, backRight, backBottom)
                    iconLeft = itemView.left + iconMargin
                    iconRight = iconLeft + (drawable?.intrinsicWidth ?: 0)
                    drawable?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                }else if(dX<0){
                    backRight=itemView.right
                    backLeft=itemView.right+dX.toInt()
                    background.setBounds(backLeft,backTop,backRight,backBottom)
                    iconRight=itemView.right-iconMargin
                    iconLeft=iconRight- (drawable?.intrinsicWidth ?: 0)
                    drawable?.setBounds(iconLeft,iconTop,iconRight,iconBottom)
                }else{
                    background.setBounds(0,0,0,0)
                    drawable?.setBounds(0,0,0,0)

                }
                background.draw(c)
                drawable?.draw(c)
            }
        })
        itemTouchHelper.attachToRecyclerView(recycleView)


        floatButton.setOnClickListener(){
            val navController=Navigation.findNavController(it)
            navController.navigate(R.id.action_wordsFragment_to_addFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WordsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WordsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}