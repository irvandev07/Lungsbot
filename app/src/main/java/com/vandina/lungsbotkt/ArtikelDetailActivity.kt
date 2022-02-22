 package com.vandina.lungsbotkt

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_artikel_detail.*

 //Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

 class ArtikelDetailActivity : AppCompatActivity() {


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_artikel_detail)

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             val w: Window = window
             w.setFlags(
                 WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                 WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
             )
         }

         val judul = intent.getStringExtra("ArtikelJudul")
         val decs = intent.getStringExtra("ArtikelDecs")
         val kategori = intent.getStringExtra("ArtikelKategori")
         val pict = intent.getStringExtra("ArtikelPict")
         val pictPembuat = intent.getStringExtra("ArtikelPictPembuat")
         val penulis = intent.getStringExtra("ArtikelPenulis")
         val tglBuat = intent.getStringExtra("ArtikelTanggalBuat")
         artikelJudul.text = judul
         artikelDesc.text = decs
         atikelKategori.text = kategori
         ArtikelPenulis.text = penulis
         ArtikelTanggalBuat.text = tglBuat
         Picasso.get().load(pict).into(artikelImg)
         Picasso.get().load(pictPembuat).into(ArtikelPictPembuat)

         btnBack.setOnClickListener {
             onBackPressed()
         }
     }

     override fun onBackPressed() {
         finish()
     }
 }