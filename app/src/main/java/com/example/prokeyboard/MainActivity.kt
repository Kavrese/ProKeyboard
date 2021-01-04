package com.example.prokeyboard

import android.content.Context
import android.graphics.Typeface
import android.graphics.Typeface.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_lin.*
import kotlinx.android.synthetic.main.lin_edittext.*
import kotlinx.android.synthetic.main.lin_indicator_list.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class MainActivity : AppCompatActivity(), MyInterface, View.OnClickListener {
    var selectedListPosition = mutableListOf<Int>()
    private var isOpenMenuSelected = false
    private var isOpenFullEditText= false
    private var isShowEditText = true
    private var ignoreKeyboard = false
    var listAllItem = mutableListOf(
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green, R.color.colorGreen)),
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green, R.color.colorGreen)),
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green, R.color.colorGreen)),
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green, R.color.colorGreen)),
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green, R.color.colorGreen)),
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green, R.color.colorGreen)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Get feedback on website design"),
        ModelItem("Get feedback on website design"),
        ModelItem("Get feedback on website design"),
        ModelItem("Get feedback on website design"),
        ModelItem("Get feedback on website design"),
        ModelItem("Buy milk", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy milk", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy milk", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Buy milk", ModelIndicator("Buy", R.drawable.indicator_red, R.color.colorRed)),
        ModelItem("Call Katherine about the trip", ModelIndicator("Work", R.drawable.indicator_purple, R.color.colorPurple)),
        ModelItem("Call Katherine about the trip", ModelIndicator("Work", R.drawable.indicator_purple, R.color.colorPurple)),
        ModelItem("Call Katherine about the trip", ModelIndicator("Work", R.drawable.indicator_purple, R.color.colorPurple))
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                }else{
                    ignoreKeyboard = false
                }
            }
        })

        chooseList.setOnClickListener {
            showInt(1)
        }

        initWindowChooseInd(Grey)
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

        view.setCompoundDrawablesWithIntrinsicBounds(view.compoundDrawables[0], Drawable.createFromPath(""), getDrawable(R.drawable.check), Drawable.createFromPath(""))
    }

    private fun initMenu(){
        menu.setOnClickListener {
            if (!isOpenMenuSelected) {
                if (selectedListPosition.size != 0) {
                    showSelectedWindow()

                    Handler().postDelayed({
                        if (selectedListPosition.size > 1){
                            motion_edit.transitionToEnd()
                        }else{
                            motion_edit.transitionToStart()
                        }
                    }, 510)

                } else {
                    Snackbar.make(menu, "Choose tasks", Snackbar.LENGTH_LONG).show()
                }
            }else{
                hideSelectedWindow()
            }
        }

        rename.setOnClickListener {
            if(checkSizeList()){

            }
        }

        status.setOnClickListener {
            if(checkSizeList()){
                motion.setTransition(R.id.tra_ind)
                motion.transitionToEnd()
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

    private fun showInt (mode: Int){
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

    private fun hideInt (mode: Int){
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

    private fun checkSizeList (): Boolean{
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

    private fun showEditText(){
            isShowEditText = true
            motion.setTransition(R.id.tra_hide)
            motion.transitionToStart()
    }

    private fun hideEditText(){
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
        initViewChooseList(p0.text.toString(), p0.compoundDrawables[0])
    }

    private fun initViewChooseList(text: String, drawable: Drawable){
        chooseList.text = text
        chooseList.setCompoundDrawablesWithIntrinsicBounds(drawable, Drawable.createFromPath(""), resources.getDrawable(R.drawable.down), Drawable.createFromPath(""))
    }

}