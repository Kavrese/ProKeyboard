package com.example.prokeyboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(var list: MutableList<Model>, val activity: MainActivity): RecyclerView.Adapter<Adapter.ViewHolder>() {
    private val listSelected = mutableListOf<Int>()
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val indicator = itemView.findViewById<ImageView>(R.id.indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rec,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.checkBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                listSelected.add(position)
            }else{
                listSelected.remove(position)
            }
            activity.getSelectedListPosition(listSelected)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
