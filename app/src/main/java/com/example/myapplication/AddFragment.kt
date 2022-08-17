package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var editTextEnglish :EditText
    private lateinit var editTextChineseMeaning : EditText
    private lateinit var buttonSubmit : Button
    private lateinit var wordViewModel: WordViewModel


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
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireActivity()
        wordViewModel=ViewModelProvider(activity).get(WordViewModel::class.java)
        buttonSubmit=activity.findViewById(R.id.buttonSubmit)
        editTextEnglish=activity.findViewById(R.id.editTextEnglish)
        editTextChineseMeaning=activity.findViewById(R.id.editTextTextChineseMeaning)
        //沒有輸入時不能添加空白
        buttonSubmit.isEnabled=false
        //跳轉到這個頁面的時候焦點直接放到editTextEnglish上
        editTextEnglish.requestFocus()
        //管理鍵盤,彈出鍵盤
        val imm : InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextEnglish,0)

        //兩個輸入框都有輸入資料後，buttonSubmit變成可以按
        val textWatcher:TextWatcher=object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val english :String = editTextEnglish.text.toString().trim()
                val chineseMeaning : String = editTextChineseMeaning.text.toString().trim()
                if(english.isNotEmpty() && chineseMeaning.isNotEmpty()){
                    buttonSubmit.isEnabled=true
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }
        editTextEnglish.addTextChangedListener(textWatcher)
        editTextChineseMeaning.addTextChangedListener(textWatcher)

        buttonSubmit.setOnClickListener(){
            val english :String = editTextEnglish.text.toString().trim()
            val chineseMeaning : String = editTextChineseMeaning.text.toString().trim()
            val word = Word(english,chineseMeaning)
            wordViewModel.insertWord(word)
            //呼叫導航跳回上一頁
            val navController= Navigation.findNavController(it)
            navController.navigateUp()


        }



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}