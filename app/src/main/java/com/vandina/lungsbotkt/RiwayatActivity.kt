package com.vandina.lungsbotkt

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vandina.lungsbotkt.data.Riwayat
import com.vandina.lungsbotkt.ui.RiwayatAdapter
import kotlinx.android.synthetic.main.activity_riwayat.*

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class RiwayatActivity : AppCompatActivity() {


    private var ref : DatabaseReference?= null
    private var firebaseUser : FirebaseUser?=null
    private var firebaseUserId : String = ""

    private lateinit var riwayatAdapter: RiwayatAdapter
    var riwayatList = ArrayList<Riwayat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        Riwayat()

        riwayatBack.setOnClickListener {
           onBackPressed()
        }

        txtBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun Riwayat() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUserId = firebaseUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("User").child(firebaseUserId).child("riwayat")

        ref!!.addListenerForSingleValueEvent(
            object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    riwayatList.clear()
                    if (p0.exists()) {
                        for (h in p0.getChildren()) {
                            val riwayat = h.getValue(Riwayat::class.java)

                            if (riwayat != null) {
                                riwayatList.add(riwayat)
                            }
                        }
                    }
                    recyclerViewInfo()
                }
            })
    }

    private fun recyclerViewInfo() {
        riwayatAdapter = RiwayatAdapter(riwayatList)
        rv_riwayat.adapter = riwayatAdapter
        rv_riwayat.layoutManager = LinearLayoutManager(
            this
        )


    }
}