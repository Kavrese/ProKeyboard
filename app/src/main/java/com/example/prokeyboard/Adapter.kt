package com.example.prokeyboard

import android.content.Context
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(var list: MutableList<ModelItem>, private val activity: MainActivity, private val selectedList: MutableList<Int>): RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val indicator: ImageView = itemView.findViewById(R.id.indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rec,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = list[position].name
        if (position in selectedList){
            holder.checkBox.isChecked = true
        }
        holder.checkBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                activity.getAddSelectedPosition(position)
            }else{
                activity.getRemoveSelectedPosition(position)
            }
        }
        holder.indicator.setImageResource(list[position].indicator.drawableId)
        holder.indicator.setOnLongClickListener {
            val v: Vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(100)
            true
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
