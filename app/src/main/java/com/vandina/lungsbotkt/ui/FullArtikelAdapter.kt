package com.vandina.lungsbotkt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.Artikel


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class FullArtikelAdapter (var artikelList: ArrayList<Artikel>) : RecyclerView.Adapter<FullArtikelAdapter.FullArtikelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullArtikelAdapter.FullArtikelViewHolder {
        return FullArtikelViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.semua_artikel_item, parent, false)
        )
    }

    inner class FullArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_decs: TextView? = null
        var txt_judul: TextView? = null
        var txt_kategori: TextView? = null
        var img_artikel: ImageView? = null

        init {
            this.txt_decs = itemView.findViewById<TextView>(R.id.semua_decs)
            this.txt_judul = itemView.findViewById<TextView>(R.id.semua_judul)
            this.txt_kategori = itemView.findViewById<TextView>(R.id.semua_kategori)
            this.img_artikel = itemView.findViewById<ImageView>(R.id.pict_artikel)

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FullArtikelAdapter.FullArtikelViewHolder, position: Int) {
        var artikel = artikelList[position]
        holder.txt_decs?.text = artikel.decs
        holder.txt_judul?.text = artikel.judul
        holder.txt_kategori?.text = artikel.kategori
        Picasso.get()
            .load(artikel.pict)
            .into(holder.img_artikel)
    }

    override fun getItemCount(): Int {
        return artikelList.size
    }
}