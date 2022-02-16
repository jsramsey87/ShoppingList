package com.example.myapplication

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.itemshoppinglist.view.*
import java.io.FileOutputStream

class ShoppingListAdapter ( private val lists: MutableList<ShoppingList>) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.itemshoppinglist,
                parent,
                false
            )
        )
    }

    fun addList(list: ShoppingList){
        lists.add(list)
        notifyItemInserted(lists.size-1)
    }

    fun deleteDoneList(){
        lists.removeAll { list ->
            list.isChecked
        }
        notifyDataSetChanged()
    }


    private fun toggleStrikeThrough(TVItem: TextView, isChecked: Boolean){
        if(isChecked){
            TVItem.paintFlags = TVItem.paintFlags or STRIKE_THRU_TEXT_FLAG
        }else{
            TVItem.paintFlags = TVItem.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val curList = lists[position]
        holder.itemView.apply {
            TVItem.text = curList.title
            CBDone.isChecked = curList.isChecked
            toggleStrikeThrough(TVItem, curList.isChecked)
            CBDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(TVItem, isChecked)
                curList.isChecked = !curList.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    fun getShoppingList(index: Int): ShoppingList{
        return lists.get(index)
    }
}