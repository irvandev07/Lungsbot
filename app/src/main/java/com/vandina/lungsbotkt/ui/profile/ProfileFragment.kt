package com.vandina.lungsbotkt.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import com.vandina.lungsbotkt.*
import com.vandina.lungsbotkt.R
import com.vandina.lungsbotkt.data.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi


class ProfileFragment : Fragment() {

    lateinit var et_nama : TextView
    lateinit var et_email : TextView
    lateinit var userFoto : CircleImageView
    lateinit var sharedPreferances: SharedPreferances
    lateinit var logout : LinearLayout
    lateinit var loadingDialog: LoadingDialog


    private var ref : DatabaseReference?= null
    private var firebaseUser : FirebaseUser?=null
    private var firebaseUserId : String = ""

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferances = SharedPreferances(requireContext())
        //val namaDepan = sharedPreferances.getSessionString(SharedPreferances.key_value)
        logout = root.findViewById(R.id.logout)
        et_nama = root.findViewById(R.id.et_nama)
        et_email = root.findViewById(R.id.et_email)
        userFoto = root.findViewById(R.id.userFoto)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUserId = firebaseUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("User").child(firebaseUserId)


        ProfileUser()

        logout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(activity).create()
            alertDialog.setTitle("Yakin ingin keluar ?")
            alertDialog.setMessage("Kamu perlu login lagi kalau ingin melanjutkan aktivitas sebelumya.")

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yakin"
            ) { dialog, which -> logoutUser() }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak"
            ) { dialog, which -> dialog.dismiss() }
            alertDialog.show()

            alertDialog.window!!.setGravity(Gravity.BOTTOM)

            val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams
        }

        root.riwayat.setOnClickListener {
            startActivity(Intent(activity, RiwayatActivity::class.java))
        }

        //root.panduan.setOnClickListener {
            //Snackbar.make(layout_profile,"Sedang dalam pengembangan",Snackbar.LENGTH_SHORT).setBackgroundTint(R.color.black_transparant).show()
        //}

        //root.infoApp.setOnClickListener {
            //Snackbar.make(layout_profile,"Sedang dalam pengembangan",Snackbar.LENGTH_SHORT).setBackgroundTint(R.color.black_transparant).show()
        //}

        root.pengaturan.setOnClickListener {
            //Snackbar.make(layout_profile,"Sedang dalam pengembangan",Snackbar.LENGTH_SHORT).setBackgroundTint(R.color.black_transparant).show()
            startActivity(Intent(activity, UpdateUserActivity::class.java))
        }

        return root
    }

    private fun ProfileUser(){

        ref!!.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user =p0.getValue(User::class.java)

                if (user != null) {
                    et_nama.text = user.nama
                    et_email.text = user.email

                    Picasso.get()
                        .load(user.profileUser)
                        .into(userFoto)
                }
            }
        })
    }

    private fun logoutUser(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(getActivity(), Login::class.java))
        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    }






}