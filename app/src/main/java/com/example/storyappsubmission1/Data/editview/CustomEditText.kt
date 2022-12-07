package com.example.storyappsubmission1.Data.editview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText


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
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    when (inputType) {
                        1 -> emailValidation(p0.toString())

                        129 -> passwordValidation(p0.toString())
                    }
                }
            }

        })

    }

    private fun passwordValidation(text: String?){
        val check = text?.length!! >= 6
        if (!check)
            error = "password less than 6"
    }

    private fun emailValidation(text: String?){
        val check = Patterns.EMAIL_ADDRESS.matcher(text).matches()
        if(!check)
            error = "email invalid"
    }





}