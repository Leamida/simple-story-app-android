package com.example.storyapp.core.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R
import java.util.regex.Pattern

class CustomEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init(){
        addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               when(inputType){
                   32->{
                       s?.let {
                           when{
                               !Pattern.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",it.trim())->{
                                   error = resources.getString(R.string.insert_email_correctly)
                               }
                               it.isBlank()->{
                                   error = resources.getString(R.string.required)
                               }
                           }
                       }
                   }
                   128->{
                       s?.let {
                           when{
                               it.length<6->{
                                   error = resources.getString(R.string.minimum_password_char)
                               }
                               it.isBlank()->{
                                   error = resources.getString(R.string.required)
                               }
                           }
                       }
                   }
               }
            }
        })
    }

}