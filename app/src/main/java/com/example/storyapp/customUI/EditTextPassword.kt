package com.example.storyapp.customUI

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputLayout

class EditTextPassword : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
    private fun init(){

        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
               error = if (s.length<8){
                   resources.getString(R.string.minimum_password)
               }else{
                   null
               }
            }

            override fun afterTextChanged(s: Editable) {
                error = if (s.length<8){
                    resources.getString(R.string.minimum_password)
                }else{
                    null
                }
            }

        })
    }
    fun isPassword(textInputLayout: TextInputLayout) : Boolean{
        return if(text.toString().isNotEmpty() && text.toString().length>=8){
            textInputLayout.helperText = ""
            true
        }else if (text.toString().isEmpty()){
            textInputLayout.helperText = "Required"
            false
        }else{
            false
        }
    }
}