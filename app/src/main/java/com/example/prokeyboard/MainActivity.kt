package com.example.prokeyboard

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_lin.*
import kotlinx.android.synthetic.main.lin_edittext.*
import kotlinx.android.synthetic.main.lin_indicator_list.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.util.*

class MainActivity : AppCompatActivity(), MyInterface, View.OnClickListener,
    TextView.OnEditorActionListener {
    var selectedListPosition = mutableListOf<Int>()
    private var isOpenMenuSelected = false
    private var isOpenFullEditText= false
    private var isShowEditText = true
    var ignoreKeyboard = false
    private var listAllItem = mutableListOf<ModelItem>()
    private var reStatusPos = -1
    var night = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sh = getSharedPreferences("0",0)
        var ed : SharedPreferences.Editor
        night = sh.getBoolean("night_mode",false)
        setNightMode(night)

        message.setOnEditorActionListener(this)
        listAllItem = mutableListOf(
            ModelItem("Go to Central Park", ModelIndicator("Go",getDrawable(R.drawable.indicator_green), R.color.colorGreen)),
            ModelItem("Buy new macbook", ModelIndicator("Buy", getDrawable(R.drawable.indicator_red), R.color.colorRed)),
            ModelItem("Get feedback on website design", ModelIndicator(drawable = getDrawable(R.drawable.indicator_grey))),
            ModelItem("Buy milk", ModelIndicator("Buy", getDrawable(R.drawable.indicator_red), R.color.colorRed)),
            ModelItem("Call Ketherine about the trip", ModelIndicator("Work", getDrawable(R.drawable.indicator_purple), R.color.colorPurple)))
        rec.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        rec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isShowEditText) {
                    Handler().postDelayed({
                        if (!isShowEditText) {
                            showEditText()
                        }
                    }, 250)
                } else {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING && isShowEditText) {
                        if (isOpenFullEditText) {
                            hideKeyboard()
                            Handler().postDelayed({
                                hideEditText()
                            }, 810)
                        } else {
                            hideEditText()
                        }
                    }
                }
            }
        })
        menu.setOnLongClickListener {
            night = !night
            setNightMode(night)

            ed = sh.edit()
            ed.putBoolean("night_mode",night)
            ed.apply()

            true
        }

        initNewAdapterForRecyclerView()
        initMenu()
        KeyboardVisibilityEvent.setEventListener(this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (!ignoreKeyboard) {
                    if (isOpen) {
                        showFullEditText()
                    } else {
                        hideFullEditText()
                    }
                    isOpenFullEditText = isOpen
                } else {
                    ignoreKeyboard = false
                }
            }
        })

        chooseList.setOnClickListener {
            showInt(1)
        }

        initWindowChooseInd(Grey)
    }

    private fun setNightMode(night: Boolean){
        if(night){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun initWindowChooseInd(view: TextView){
        Green.setCompoundDrawablesWithIntrinsicBounds(R.drawable.indicator_green, 0, 0, 0)
        Green.typeface = DEFAULT

        Red.setCompoundDrawablesWithIntrinsicBounds(R.drawable.indicator_red, 0, 0, 0)
        Red.typeface = DEFAULT

        Purple.setCompoundDrawablesWithIntrinsicBounds(R.drawable.indicator_purple, 0, 0, 0)
        Purple.typeface = DEFAULT

        Grey.setCompoundDrawablesWithIntrinsicBounds(R.drawable.indicator_grey, 0, 0, 0)
        Grey.typeface = DEFAULT

        view.typeface = DEFAULT_BOLD

        view.setCompoundDrawablesWithIntrinsicBounds(
            view.compoundDrawables[0], Drawable.createFromPath(
                ""
            ), getDrawable(R.drawable.check), Drawable.createFromPath("")
        )
    }

    private fun showSnackBar(text: String, duration: Int){
        val snackbar = Snackbar.make(lin_main, text, duration)
        val textView: TextView = snackbar.view.findViewById(R.id.snackbar_text)
        textView.setTextColor(getColorFromAttr(R.attr.colorAccent))
        snackbar.view.setBackgroundColor(getColorFromAttr(R.attr.colorPrimary))
        snackbar.show()
    }

    @ColorInt
    fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    private fun initFullEditText(){
        message.setText("")
        initViewChooseList("Not assigned", getDrawable(R.drawable.indicator_grey)!!)
    }

    private fun initMenu(){
        menu.setOnClickListener {
            if (!isOpenMenuSelected) {
                if (selectedListPosition.size != 0) {
                    showSelectedWindow()

                    Handler().postDelayed({
                        if (selectedListPosition.size > 1) {
                            motion_edit.transitionToEnd()
                        } else {
                            motion_edit.transitionToStart()
                        }
                    }, 510)

                } else {
                    showSnackBar("Choose tasks", Snackbar .LENGTH_LONG)
                }
            }else{
                hideSelectedWindow()
            }
        }

        status.setOnClickListener {
            if(checkSizeListOnlyOne()){
                motion.setTransition(R.id.tra_ind)
                motion.transitionToEnd()
                reStatusPos = selectedListPosition[0]
            }
        }

        delete.setOnClickListener{
            hideSelectedWindow()

            for (i in 0 until selectedListPosition.size){
                var maxPos = i
                for (j in (i + 1) until selectedListPosition.size){
                    if (selectedListPosition[j] > selectedListPosition[maxPos]){
                        maxPos = j
                    }
                }
                val dop = selectedListPosition[i]
                selectedListPosition[i] = selectedListPosition[maxPos]
                selectedListPosition[maxPos] = dop
            }

            for (i in selectedListPosition){
                listAllItem.removeAt(i)
            }
            selectedListPosition.clear()
            initNewAdapterForRecyclerView()
        }
    }

    private fun showInt(mode: Int){
        if (mode == 0){     //Обычное открытие
            motion.setTransition(R.id.tra_ind)
            motion.transitionToEnd()
        }else{          //Full
            if (mode == 1) {
                motion.setTransition(R.id.tra_int_full)
                motion.transitionToEnd()
                ignoreKeyboard = true
                hideKeyboard()
            }
        }
    }

    private fun hideInt(mode: Int){
        if (mode == 0){     //Обычное закрытие
            motion.setTransition(R.id.tra_ind)
            motion.transitionToStart()
        }else{              //Full
            if (mode == 1) {
                motion.setTransition(R.id.tra_int_full)
                motion.transitionToStart()
                message.requestFocus()
            }
        }
    }

    private fun checkSizeListOnlyOne (): Boolean{
        return selectedListPosition.size == 1
    }

    private fun showFullEditText(){
        motion.setTransition(R.id.tra_open)
        motion.transitionToEnd()
        motion_edittext.transitionToEnd()
    }

    private fun hideFullEditText(){
        motion.setTransition(R.id.tra_open)
        motion.transitionToStart()
        motion_edittext.transitionToStart()
    }

    private fun showSelectedWindow(){
        motion.setTransition(R.id.tra_menu)
        motion.transitionToEnd()
        isOpenMenuSelected = true
    }

    private fun hideSelectedWindow(){
        motion.setTransition(R.id.tra_menu)
        motion.transitionToStart()
        isOpenMenuSelected = false
    }

    fun showEditText(){
            isShowEditText = true
            motion.setTransition(R.id.tra_hide)
            motion.transitionToStart()
    }

   fun hideEditText(){
        if (isOpenFullEditText){
            hideFullEditText()
        }
            isShowEditText = false
            motion.setTransition(R.id.tra_hide)
            motion.transitionToEnd()
    }

    private fun initNewAdapterForRecyclerView (){
        rec.adapter = Adapter(listAllItem, this@MainActivity, selectedListPosition)
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun showKeyboard(){
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(message, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun getAddSelectedPosition(position: Int) {
        selectedListPosition.add(position)
        if (isOpenMenuSelected) {
            if (selectedListPosition.size > 1) {
                motion_edit.transitionToEnd()
            }
        }
    }

    override fun getRemoveSelectedPosition(position: Int) {
        selectedListPosition.remove(position)
        initNewAdapterForRecyclerView()
        if (selectedListPosition.size == 1 && isOpenMenuSelected){
            motion_edit.transitionToStart()
        }
        if (selectedListPosition.size == 0) {
            hideSelectedWindow()
        }
    }

    override fun onClick(p0: View?) {
        initWindowChooseInd(p0 as TextView)
        if (reStatusPos == -1) {
            initViewChooseList(p0.text.toString(), p0.compoundDrawables[0])
        }else{
            listAllItem[reStatusPos].indicator = ModelIndicator(p0.text.toString(), p0.compoundDrawables[0], getColorFromNameCategory(p0.text.toString()))
            reStatusPos = -1
            selectedListPosition.clear()
            initNewAdapterForRecyclerView()
            hideInt(0)
        }
    }

    private fun initViewChooseList(text: String, drawable: Drawable){
        chooseList.text = text
        chooseList.setCompoundDrawablesWithIntrinsicBounds(
            drawable, Drawable.createFromPath(""), resources.getDrawable(R.drawable.down), Drawable.createFromPath("")
        )
        hideInt(1)
        showKeyboard()
    }

    private fun getColorFromNameCategory(name: String): Int{
        var colorId = R.color.colorGrey
        when (name){
            "Go" -> colorId = R.color.colorGreen
            "Buy" -> colorId = R.color.colorRed
            "Work" -> colorId = R.color.colorPurple
        }
        return colorId
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        return if (p1 == EditorInfo.IME_ACTION_DONE && message.text.isNotEmpty()){
            val save = ModelItem(message.text.toString(), ModelIndicator(chooseList.text.toString(), chooseList.compoundDrawables[0], getColorFromNameCategory(chooseList.text.toString())))
            listAllItem.add(save)
            initNewAdapterForRecyclerView()
            initFullEditText()
            initWindowChooseInd(Grey)
            hideKeyboard()
            true
        }else{
            false
        }
    }

    override fun clickEditTextItem(position: Int, editText: EditText) {
        ignoreKeyboard = true
        hideEditText()
        editText.setOnEditorActionListener { _, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE){
                val newName = editText.text.toString()
                listAllItem[position].name = newName
                initNewAdapterForRecyclerView()
                return@setOnEditorActionListener true
            }else{
                return@setOnEditorActionListener false
            }
        }
    }
}