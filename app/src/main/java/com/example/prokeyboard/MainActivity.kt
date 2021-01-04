package com.example.prokeyboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_lin.*

class MainActivity : AppCompatActivity(), MyInterface {
    var selectedListPosition = mutableListOf<Int>()
    var isOpenMenuSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rec.apply {
            adapter = Adapter(mutableListOf(
                ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green)),
                ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red)),
                ModelItem("Get feedback on website design"),
                ModelItem("Buy milk", ModelIndicator("Buy", R.drawable.indicator_red)),
                ModelItem("Call Katherine about the trip", ModelIndicator("Work", R.drawable.indicator_purple))
            ), this@MainActivity)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        menu.setOnClickListener {
            if (!isOpenMenuSelected) {
                if (selectedListPosition.size != 0) {
                    showSelectedWindow()
                } else {
                    Snackbar.make(menu, "Choose tasks", Snackbar.LENGTH_LONG).show()
                }
            }else{
                hideSelectedWindow()
            }
        }
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

    override fun getSelectedListPosition(list: MutableList<Int>) {
        selectedListPosition = list
        if (list.size == 0 && isOpenMenuSelected){
            hideSelectedWindow()
        }else {
            if (list.size > 1) {
                rename!!.visibility = GONE
                status!!.visibility = GONE
            } else {
                rename!!.visibility = VISIBLE
                status!!.visibility = VISIBLE
            }
        }
    }
}