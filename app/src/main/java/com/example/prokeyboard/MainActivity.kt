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
    var listAllItem = mutableListOf(
        ModelItem("Go to Central Park", ModelIndicator("Go", R.drawable.indicator_green)),
        ModelItem("Buy new macbook", ModelIndicator("Buy", R.drawable.indicator_red)),
        ModelItem("Get feedback on website design"),
        ModelItem("Buy milk", ModelIndicator("Buy", R.drawable.indicator_red)),
        ModelItem("Call Katherine about the trip", ModelIndicator("Work", R.drawable.indicator_purple))
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rec.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        initNewAdapterForRecyclerView()
        initMenu()
    }

    private fun initMenu(){
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

        rename.setOnClickListener {

        }

        status.setOnClickListener {

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

    private fun initNewAdapterForRecyclerView (){
        rec.adapter = Adapter(listAllItem, this@MainActivity, selectedListPosition)
    }

    override fun getAddSelectedPosition(position: Int) {
        selectedListPosition.add(position)
        if (selectedListPosition.size == 0 && isOpenMenuSelected){
            hideSelectedWindow()
        }else {
            if (selectedListPosition.size > 1) {
                motion_edit.transitionToEnd()
            }
        }
    }

    override fun getRemoveSelectedPosition(position: Int) {
        selectedListPosition.remove(position)
        initNewAdapterForRecyclerView()
        if (selectedListPosition.size == 1) {
            motion_edit.transitionToStart()
        }
    }
}