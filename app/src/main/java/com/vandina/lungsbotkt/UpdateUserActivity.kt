package com.vandina.lungsbotkt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.vandina.lungsbotkt.data.User
import kotlinx.android.synthetic.main.activity_update_user.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.toast_item.view.*

//Project by Irvan Syachrialdi / vandina project copyright 2020
//https://github.com/irvansychrldi

class UpdateUserActivity : AppCompatActivity() {

    private val RequestCode = 438
    private var imageUri : Uri?= null
    private var storageRef : StorageReference?=null

    private var ref : DatabaseReference?= null
    private var firebaseUser : FirebaseUser?=null
    private var firebaseUserId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUserId = firebaseUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("User").child(firebaseUserId)
        storageRef = FirebaseStorage.getInstance().reference.child("profileUser")

        ProfileUser()

        lBack.setOnClickListener {
            onBackPressed()
        }

        edit_foto.setOnClickListener{
            pickImage()
        }

        simpanUpdate.setOnClickListener {
            update()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun ProfileUser() {


        ref!!.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)

                //val email:String = p0.child("email").getValue().toString()
                val nama = p0.child("nama").getValue().toString()
                val pass = p0.child("password").getValue().toString()
                val ulangPass = p0.child("passwordUlang").getValue().toString()

                //updateEmail.setText(email)
                updateNama.setText(nama)
                updatePassword.setText(pass)
                updateUlangPassword.setText(ulangPass)

                if (user != null) {
                    Picasso.get()
                        .load(user.profileUser)
                        .into(edit_foto)
                }
            }
        })
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down)
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_item,
            null)
        layout.startAnimation(slide_down)
        val toast = Toast(getApplicationContext())

        if(requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null){
            imageUri = data.data
            layout.txtToastCustom.setText("Berhasil mengganti foto")
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
            uploadImage()
        }
    }

    private fun uploadImage() {

        val fileRef = storageRef!!.child(System.currentTimeMillis().toString()+".jpg")

        if(imageUri != null){
            var uploadTask : StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if(task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val mapProfileImg = HashMap<String,Any>()
                    mapProfileImg["profileUser"] = url
                    ref!!.updateChildren(mapProfileImg)
                }
            }
        }
    }

    private fun update(){

        val slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down)
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.toast_item,
            null)
        layout.startAnimation(slide_down)
        val toast = Toast(getApplicationContext())

        val nama = updateNama.text.toString()
        //val email = updateEmail.text.toString()
        val pass = updatePassword.text.toString()
        val ulangPass = updateUlangPassword.text.toString()

        val userHashMap = HashMap<String, Any>()
        userHashMap["nama"] = nama
        //userHashMap["email"] = email
        userHashMap["password"] = pass
        userHashMap["passwordUlang"] = ulangPass

        ref!!.updateChildren(userHashMap)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    layout.txtToastCustom.setText("Berhasil merubah profile")
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.duration = Toast.LENGTH_LONG
                    toast.view = layout
                    toast.show()
                }
            }

    }
}