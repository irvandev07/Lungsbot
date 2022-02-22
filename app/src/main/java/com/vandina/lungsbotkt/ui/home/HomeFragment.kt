package com.vandina.lungsbotkt.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.vandina.lungsbotkt.*
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.Artikel
import com.vandina.lungsbotkt.data.Informasi
import com.vandina.lungsbotkt.data.User
import com.vandina.lungsbotkt.ui.ArtikelAdapter
import com.vandina.lungsbotkt.ui.InformasiAdapter
import com.vandina.lungsbotkt.ui.profile.ProfileFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_chatbot.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi


class HomeFragment : Fragment() {

    lateinit var set_nama : TextView
    lateinit var profile_user : CircleImageView
    private lateinit var rv_artikel: RecyclerView
    private lateinit var rv_informasi: RecyclerView
    private lateinit var txt_gretting  : TextView
    private lateinit var grettingImg : ImageView

    private var ref : DatabaseReference?= null
    private var firebaseUser : FirebaseUser?=null
    private var firebaseUserId : String = ""

    var artikelList = ArrayList<Artikel>()
    var informasiList = ArrayList<Informasi>()


    private lateinit var artikelAdapter: ArtikelAdapter
    private lateinit var informasiAdapter: InformasiAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

            rv_artikel = root.findViewById(R.id.rv_artikel) as RecyclerView
            rv_informasi = root.findViewById(R.id.rv_informasi) as RecyclerView
            txt_gretting = root.findViewById(R.id.txt_gretting) as TextView
            grettingImg = root.findViewById(R.id.grettingImg)
            set_nama = root.findViewById(R.id.set_nama)
            profile_user = root.findViewById(R.id.profile_user)

            //root.txt_fullArtikel.setOnClickListener {
              //  startActivity(Intent(activity,ArtikelActivity::class.java))
            //}

            activity?.window!!.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            activity?.window!!.statusBarColor = Color.TRANSPARENT
            activity?.window!!.navigationBarColor = Color.BLACK

            profileUser()
            gretting()

            artikel()
            informasi()


        return root
    }

    private fun profileUser(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().getReference("User")
        firebaseUserId = firebaseUser!!.uid

        ref!!.child(firebaseUserId).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user =p0.getValue(User::class.java)

                if (user != null) {
                    set_nama.text = user.nama

                    Picasso.get()
                        .load(user.profileUser)
                        .into(profile_user)
                }
            }
        })
    }

    private fun gretting() {
        val calendar: Calendar = Calendar.getInstance()
        val timeOfDay: Int = calendar.get(Calendar.HOUR_OF_DAY)

        if (timeOfDay >= 0 && timeOfDay < 12) {
            txt_gretting.setText("Selamat Pagi,")
            grettingImg.setImageResource(R.drawable.morning)
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            txt_gretting.setText("Selamat Siang,")
            grettingImg.setImageResource(R.drawable.noon)
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            txt_gretting.setText("Selamat Sore,")
            grettingImg.setImageResource(R.drawable.afternoon)
        } else if (timeOfDay >= 18 && timeOfDay < 24) {
            txt_gretting.setText("Selamat Malam,")
            //greetText.setTextColor(Color.WHITE)
            grettingImg.setImageResource(R.drawable.night)
        }
    }

    private fun informasi() {
        ref = FirebaseDatabase.getInstance().getReference("Informasi")
        ref!!.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                informasiList.clear()

                for (h in p0.children) {
                    val info = h.getValue(Informasi::class.java)

                    if (info != null) {
                        informasiList.add(info)
                    }
                }
                recyclerViewInfo()
            }
        })
    }

    private fun recyclerViewInfo() {
        informasiAdapter = InformasiAdapter(requireContext(), informasiList)
        rv_informasi.adapter = informasiAdapter
        rv_informasi.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
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
                recyclerView(artikelList)
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun recyclerView(list: ArrayList<Artikel>) {
        artikelAdapter = ArtikelAdapter(requireContext(), list)
        rv_artikel.adapter = artikelAdapter
        rv_artikel.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)

        artikelAdapter.onItemClick = {artikel ->
            //Snackbar.make(activity_home,"anda menekan ${artikel.judul}",Snackbar.LENGTH_SHORT).setBackgroundTint(R.color.black_transparant).show()

            val i = Intent(activity, ArtikelDetailActivity::class.java)
            i.putExtra("ArtikelKategori",artikel.kategori)
            i.putExtra("ArtikelJudul",artikel.judul)
            i.putExtra("ArtikelDecs",artikel.decs)
            i.putExtra("ArtikelPict",artikel.pict)
            i.putExtra("ArtikelPictPembuat",artikel.pictPembuat)
            i.putExtra("ArtikelTanggalBuat",artikel.tanggalBuat)
            i.putExtra("ArtikelPenulis",artikel.penulis)
            startActivity(i)
        }

    }
}