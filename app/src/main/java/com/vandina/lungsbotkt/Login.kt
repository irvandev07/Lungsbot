package com.vandina.lungsbotkt

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.vandina.lungsbotkt.R.color
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.txt_signUp
import kotlinx.android.synthetic.main.activity_regis.*
import kotlinx.android.synthetic.main.panduan_bottom_sheet.*
import kotlinx.android.synthetic.main.panduan_bottom_sheet.view.*
import kotlinx.android.synthetic.main.toast_item.*
import kotlinx.android.synthetic.main.toast_item.view.*

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class Login : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var parentLayout : View
    lateinit var progressDialog: ProgressDialog
    lateinit var loadingDialog: LoadingDialog
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var bottomSheetView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        parentLayout = findViewById(android.R.id.content)



        mAuth = FirebaseAuth.getInstance()
        txt_signUp.setOnClickListener {
            startActivity(Intent(this@Login, Registrasi::class.java))
        }

        btn_Login.setOnClickListener{
            dataLogin()
        }

        panduanLogin.setOnClickListener{
            bottomSheet()
        }

    }

    private fun bottomSheet() {
        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.panduan_bottom_sheet,
            bottomSheetContainer,
            false
        )

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

    }

    private fun dataLogin(){

        val email = get_emailLogin.text.toString()
        val password = get_passLogin.text.toString()
        val slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down)
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_item,
            null)
        layout.startAnimation(slide_down)
        val toast = Toast(getApplicationContext())

        if (email == ""){
            //Snackbar.make(parentLayout, "oopss, email belum di masukan", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_transparant)).show()

            layout.txtToastCustom.setText("Silahkan masukan email anda")
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()

        }else if (password == ""){
            //Snackbar.make(parentLayout, "oopss,password belum di masukan", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor( color.red_transparant)).show()
            layout.txtToastCustom.setText("Silahkan masukan password anda")
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else if (password.length < 8){
            //Snackbar.make(parentLayout, "oopss, password minimal 8 huruf/karakter", Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(color.red_400)).show()
            layout.txtToastCustom.setText("Password minimal 8 huruf/angka")
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                       // progressDialog = ProgressDialog(this)
                        //progressDialog.setTitle("Mohon tunggu")
                        //progressDialog.window!!.setGravity(Gravity.BOTTOM)
                        //progressDialog.show()

                        loadingDialog = LoadingDialog(this)
                        loadingDialog.startLoadingDialog()

                        startActivity(Intent(this@Login, MainActivity::class.java))
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()

                    }else{
                        layout.txtToastCustom.setText("Email atau password anda salah!")
                        toast.setGravity(Gravity.TOP, 0, 0)
                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout
                        toast.show()
                    }
                }
        }

    }
}