package com.vandina.lungsbotkt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.Jawaban
import kotlinx.android.synthetic.main.selection_item.view.*


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class SelectionAdapter(context: Context,var jawabanList: ArrayList<Jawaban>)  : RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder>() {

    var onItemClick: ((Jawaban) -> Unit)? = null
    inner class SelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txt_selection: TextView? = null

        init {
            this.txt_selection = itemView.findViewById<TextView>(R.id.txt_selection)
            //itemView.txt_selection.paintFlags = itemView.txt_selection.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            itemView.setOnClickListener {
                onItemClick?.invoke(jawabanList[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionAdapter.SelectionViewHolder {
        return SelectionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.selection_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return jawabanList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SelectionViewHolder, position: Int) {
        var jawab = jawabanList[position]
        holder.txt_selection?.text = jawab.jawaban
    }

}