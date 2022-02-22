package com.vandina.lungsbotkt

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_regis.*
import kotlinx.android.synthetic.main.panduan_bottom_sheet.*
import kotlinx.android.synthetic.main.toast_item.view.*

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class Registrasi : AppCompatActivity() {

    lateinit var ref : DatabaseReference
    lateinit var mAuth: FirebaseAuth
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetView: View

    lateinit var parentLayout : View
    lateinit var progressDialog : ProgressDialog

    private var firebaseUserId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regis)

        parentLayout = findViewById(android.R.id.content)

        mAuth = FirebaseAuth.getInstance()

        txt_signIn.setOnClickListener {
            startActivity(Intent(this@Registrasi, Login::class.java))
        }

        btn_Regis.setOnClickListener{
            registerUser()
        }

        panduanRegis.setOnClickListener {
            bottomSheet()
        }

    }

    private fun bottomSheet() {
        bottomSheetDialog = BottomSheetDialog(this,R.style.BottomSheetDialogTheme)

        bottomSheetView = LayoutInflater.from(applicationContext).inflate(R.layout.panduan_bottom_sheet,bottomSheetContainer,false)

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

    }

    private fun registerUser(){

        val nama = get_nama.text.toString()
        val email = get_email.text.toString()
        val password = get_pass.text.toString()
        val passwordUlang = get_passUlang.text.toString()
        //val userId = ref.push().key.toString()
        val slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down)
        val layout:View = layoutInflater.inflate(R.layout.toast_item,null,false)
        layout.startAnimation(slide_down)
        val toast = Toast(this)


        if (nama == ""){
            //Snackbar.make(parentLayout, "oopss, nama belum di masukan", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()
            layout.txtToastCustom.setText("Silahkan masukan nama anda")
            toast.setGravity(Gravity.TOP,0,0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else if (email == ""){
            //Snackbar.make(parentLayout, "oopss, email belum di masukan", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()
            layout.txtToastCustom.setText("Silahkan masukan email anda")
            toast.setGravity(Gravity.TOP,0,0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else if (password == ""){
            //Snackbar.make(parentLayout, "oopss, password belum di masukan", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()
            layout.txtToastCustom.setText("Silahkan masukan password anda")
            toast.setGravity(Gravity.TOP,0,0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else if (password.length < 8){
            //Snackbar.make(parentLayout, "oopss, password minimal 8 huruf/karakter", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()
            layout.txtToastCustom.setText("Password minimal 8 huruf/angka ")
            toast.setGravity(Gravity.TOP,0,0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else if (!password.equals(passwordUlang)){
            //Snackbar.make(parentLayout, "oopss, password tidak sama", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()
            layout.txtToastCustom.setText("Password yang anda masukan tidak sama")
            toast.setGravity(Gravity.TOP,0,0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else if (passwordUlang == ""){
            //Snackbar.make(parentLayout, "Ulangi Password", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()
            layout.txtToastCustom.setText("Silahkan ulangi password anda")
            toast.setGravity(Gravity.TOP,0,0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else{
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        progressDialog = ProgressDialog(this)
                        progressDialog.setTitle("Mohon tunggu")
                        progressDialog.window!!.setGravity(Gravity.BOTTOM)
                        progressDialog.show()

                        firebaseUserId = mAuth.currentUser!!.uid
                        ref = FirebaseDatabase.getInstance().reference.child("User").child(
                            firebaseUserId
                        )

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserId
                        userHashMap["profileUser"] = "https://firebasestorage.googleapis.com/v0/b/lungsbot.appspot.com/o/profileUser%2Fputih.jpg?alt=media&token=9a785a26-d948-4f7d-a936-7c903a5f6bf0"
                        userHashMap["nama"] = nama
                        userHashMap["email"] = email
                        userHashMap["password"] = password
                        userHashMap["passwordUlang"] = passwordUlang

                        progressDialog.dismiss()
                        //Snackbar.make(activity_login,"Registrasi berhasil",Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.green_transparant)).show()
                        layout.txtToastCustom.setText("Registrasi anda berhasil! silahkan login")
                        toast.setGravity(Gravity.TOP,0,0)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout
                        toast.show()

                        ref.updateChildren(userHashMap)
                            .addOnCompleteListener { task->
                                if (task.isSuccessful){
                                    startActivity(Intent(this@Registrasi, Login::class.java))
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    finish()
                                }
                            }

                    }else{
                        //Snackbar.make(parentLayout, "oopss, Registrasi kamu gagal !"+task.exception, Snackbar.LENGTH_SHORT).show()
                        layout.txtToastCustom.setText("Registrasi anda gagal! silahkan ulangi")
                        toast.setGravity(Gravity.TOP,0,0)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout
                        toast.show()
                    }
                }
        }


    }

}