package com.example.prokeyboard

import android.widget.EditText
import java.text.FieldPosition

interface MyInterface{
    fun getAddSelectedPosition(position: Int){}
    fun getRemoveSelectedPosition(position: Int){}
    fun clickEditTextItem(position: Int, editText: EditText)
}