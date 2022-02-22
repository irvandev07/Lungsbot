package com.vandina.lungsbotkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.vandina.lungsbotkt.data.Artikel
import com.vandina.lungsbotkt.ui.ArtikelAdapter
import com.vandina.lungsbotkt.ui.FullArtikelAdapter
import com.vandina.lungsbotkt.ui.InformasiAdapter
import kotlinx.android.synthetic.main.activity_artikel.*


//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class ArtikelActivity : AppCompatActivity() {

    var artikelList = ArrayList<Artikel>()
    private lateinit var fullartikelAdapter: FullArtikelAdapter
    private var ref : DatabaseReference?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artikel)


        artikel()
    }

    private fun recyclerViewArtikel(){
        fullartikelAdapter = FullArtikelAdapter(artikelList)
        rv_semuaArtikel.adapter = fullartikelAdapter
        rv_semuaArtikel.layoutManager = LinearLayoutManager(this)
    }

    private fun artikel() {
        ref = FirebaseDatabase.getInstance().getReference("Artikel")
        ref!!.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                artikelList.clear()

                for (h in p0.children) {
                    val artikel = h.getValue(Artikel::class.java)

                    if (artikel != null) {
                        artikelList.add(artikel)
                    }
                }
                recyclerViewArtikel()
            }
        })
    }
}