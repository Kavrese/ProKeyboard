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
    var isOpenMenu = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rec.apply {
            adapter = Adapter(mutableListOf(
                Model("Go to Central Park"),
                Model("Buy new macbook"),
                Model("Get feedback on website design"),
                Model("Buy milk"),
                Model("Call Katherine about the trip")
            ), this@MainActivity)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        menu.setOnClickListener {
            isOpenMenu = if (!isOpenMenu) {
                if (selectedListPosition.size != 0) {
                    motion.setTransition(R.id.tra_menu)
                    motion.transitionToEnd()
                    true
                } else {
                    Snackbar.make(menu, "Choose tasks", Snackbar.LENGTH_LONG).show()
                    false
                }
            }else{
                motion.setTransition(R.id.tra_menu)
                motion.transitionToStart()
                false
            }
        }
    }

    override fun getSelectedListPosition(list: MutableList<Int>) {
        selectedListPosition = list
        if (list.size == 0 && isOpenMenu){
            motion.setTransition(R.id.tra_menu)
            motion.transitionToStart()
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