package com.vandina.lungsbotkt.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.Riwayat


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class RiwayatAdapter (var riwayatList: ArrayList<Riwayat>) : RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatAdapter.RiwayatViewHolder {
        return RiwayatViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.riwayat_item, parent, false)
        )
    }

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var get_hasil: TextView? = null
        //var get_rid: TextView? = null
        var get_jawab: TextView? = null
        //var get_pertanyaan: TextView? = null
        var get_waktu: TextView? = null

        init {
            //this.get_rid = itemView.findViewById<TextView>(R.id.get_rid)
            this.get_hasil = itemView.findViewById<TextView>(R.id.get_hasil)
            this.get_jawab = itemView.findViewById<TextView>(R.id.get_jawab)
            //this.get_pertanyaan = itemView.findViewById<TextView>(R.id.get_pertanyaan)
            this.get_waktu = itemView.findViewById<TextView>(R.id.get_waktu)



        }
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RiwayatAdapter.RiwayatViewHolder, position: Int) {
        var riwayat = riwayatList[position]
        //holder.get_rid?.text = riwayat.rid.toString()
        holder.get_waktu?.text = riwayat.waktu
        holder.get_hasil?.text = riwayat.hasilDiagnosa
        holder.get_jawab?.text = riwayat.tanyaJawab
        //holder.get_pertanyaan?.text = riwayat.messageList
    }

    override fun getItemCount(): Int {
        return riwayatList.size
    }
}