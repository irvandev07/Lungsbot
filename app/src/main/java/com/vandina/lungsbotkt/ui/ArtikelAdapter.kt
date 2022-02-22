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
import com.vandina.lungsbotkt.data.Jawaban

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class ArtikelAdapter (private val context: Context,var artikelList: ArrayList<Artikel>) : RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder>() {

    var onItemClick: ((Artikel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtikelAdapter.ArtikelViewHolder {
        return ArtikelViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.artikel_item, parent, false)
        )
    }

    inner class ArtikelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_decs: TextView? = null
        var txt_judul: TextView? = null
        var txt_kategori: TextView? = null
        var img_artikel: ImageView? = null

        init {
            this.txt_decs = itemView.findViewById<TextView>(R.id.txt_decs)
            this.txt_judul = itemView.findViewById<TextView>(R.id.txt_judul)
            this.txt_kategori = itemView.findViewById<TextView>(R.id.txt_kategori)
            this.img_artikel = itemView.findViewById<ImageView>(R.id.img_artikel)

            itemView.setOnClickListener {
                onItemClick?.invoke(artikelList[adapterPosition])
            }
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArtikelAdapter.ArtikelViewHolder, position: Int) {
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