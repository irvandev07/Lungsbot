package com.vandina.lungsbotkt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.squareup.picasso.Picasso
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.Artikel
import com.vandina.lungsbotkt.data.Informasi


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class InformasiAdapter (private val context: Context, var infromasiList: ArrayList<Informasi>) : RecyclerView.Adapter<InformasiAdapter.InformasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformasiAdapter.InformasiViewHolder {
        return InformasiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.infromasi_item, parent, false)
        )
    }

    inner class InformasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_decs: TextView? = null
        var txt_judul: TextView? = null
        var bg_info : ImageView?= null

        init {
            this.txt_decs = itemView.findViewById<TextView>(R.id.txt_decs_info)
            this.txt_judul = itemView.findViewById<TextView>(R.id.txt_judul_info)
            this.bg_info = itemView.findViewById(R.id.bg_info)

        }
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: InformasiAdapter.InformasiViewHolder, position: Int) {
        var informasi = infromasiList[position]
        holder.txt_decs?.text = informasi.decs
        holder.txt_judul?.text = informasi.judul
        Picasso.get()
            .load(informasi.background)
            .into(holder.bg_info)
    }

    override fun getItemCount(): Int {
       return infromasiList.size
    }
}