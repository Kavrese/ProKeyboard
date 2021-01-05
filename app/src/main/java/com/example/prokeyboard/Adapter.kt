package com.example.prokeyboard

import android.content.Context
import android.os.Vibrator
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lin_edittext.*


class Adapter(
    var list: MutableList<ModelItem>,
    private val activity: MainActivity,
    private val selectedList: MutableList<Int>
): RecyclerView.Adapter<Adapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name: EditText = itemView.findViewById(R.id.name)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val indicator: ImageView = itemView.findViewById(R.id.indicator)
        val nameList: TextView = itemView.findViewById(R.id.name_list)
        val motionItem: MotionLayout = itemView.findViewById(R.id.motion_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rec, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var openListName = false
        holder.name.setText(list[position].name)
        if (position in selectedList){
            holder.checkBox.isChecked = true
        }
        holder.nameList.text = list[position].indicator.name
        holder.nameList.setTextColor(activity.resources.getColor(list[position].indicator.colorId))

        holder.checkBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                activity.getAddSelectedPosition(position)
            }else{
                activity.getRemoveSelectedPosition(position)
            }
        }

        holder.indicator.setImageDrawable(list[position].indicator.drawable)

        holder.indicator.setOnLongClickListener {
            if (!openListName) {
                holder.motionItem.transitionToEnd()
            }else{
                holder.motionItem.transitionToStart()
            }
            openListName = !openListName
            true
        }

        holder.name.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (p1){
                activity.ignoreKeyboard = true
                activity.hideEditText()
            }
        }
        holder.name.setOnEditorActionListener { p0, p1, p2 ->
            if(p1 == EditorInfo.IME_ACTION_DONE){
                val newName =  holder.name.text.toString()
                list[position].name = newName
                notifyDataSetChanged()
                activity.lin_main.requestFocus()
                activity.showEditText()
                return@setOnEditorActionListener true
            }else{
                return@setOnEditorActionListener false
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}
