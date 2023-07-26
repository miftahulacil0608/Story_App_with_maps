package com.example.storyapp.customUI

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputLayout

class EditTextCustom : AppCompatEditText, View.OnTouchListener {
    private lateinit var clearButtonImage : Drawable
    constructor(context : Context):super(context){
        init()
    }
    constructor(context: Context, attrs : AttributeSet) : super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr : Int):super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        hint = resources.getString(R.string.textInputEmailLogin)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init(){
        clearButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24)as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) showClearButton()else hideClearButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    fun isEmail(textInputLayout: TextInputLayout) : Boolean{
        return if (Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()){
            textInputLayout.helperText = ""
            true
        }else if (text.toString().isEmpty()){
            textInputLayout.helperText = "Required"
            false
        }else{
            textInputLayout.helperText = "Email Not Valid"
            false
        }

    }


    private fun showClearButton(){
        setButtonDrawables(endOfText = clearButtonImage)
    }
    private fun hideClearButton(){
        setButtonDrawables()
    }

    private fun setButtonDrawables(

        startOfTheText: Drawable? = null,

        topOfText: Drawable?=null,

        endOfText: Drawable?=null,

        bottomOfText: Drawable?=null
    )
    {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfText,
            endOfText,
            bottomOfText
        )

    }
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null){
            val clearButtonStart : Float
            val clearButtonEnd : Float
            var isClearButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL){
                clearButtonEnd = (clearButtonImage.intrinsicWidth+paddingStart).toFloat()
                when{
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            }else{
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                when{
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked){
                when(event.action){
                    MotionEvent.ACTION_DOWN->{
                        clearButtonImage = ContextCompat.getDrawable(context,R.drawable.ic_baseline_close_24) as Drawable
                        showClearButton()
                        return true
                    }
                    MotionEvent.ACTION_UP ->{
                        clearButtonImage = ContextCompat.getDrawable(context,R.drawable.ic_baseline_close_24)as Drawable
                        when{
                            text != null -> text?.clear()
                        }
                        hideClearButton()
                        return true
                    }
                    else -> return false
                }
            }else return false
        }
        return false
    }
}