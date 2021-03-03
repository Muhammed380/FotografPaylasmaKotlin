package com.muhammed.fotografpaylasmakotlin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf.*
import java.util.*
import java.util.jar.Manifest

class FotografActivity : AppCompatActivity() {

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    fun fotografpaylas (view: View) {
//Depolama İşlemleri
        val uuid  = UUID.randomUUID()
        val gorselİsmi = "$uuid.jpg"

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference

        val imageReference = reference.child("images").child(gorselİsmi)

        if (secilenGorsel != null) {
                imageReference.putFile(secilenGorsel!!).addOnSuccessListener { 
                    val yuklenenGorselReferenasi  = FirebaseStorage.getInstance().reference.child("images").child(gorselİsmi)
                    yuklenenGorselReferenasi.downloadUrl.addOnSuccessListener {
                        val dowloadUrl  = it.toString()
                        //Database İşlemleri

                        val postHashMap = hashMapOf<String,Any>()
                        postHashMap.put("gorselurl",dowloadUrl)
                        postHashMap.put("kullaniciemail",auth.currentUser.email.toString())
                        postHashMap.put("kullaniciyorum",yorumtext.text.toString())
                        postHashMap.put("tarih",Timestamp.now())

                        db.collection("Post").add(postHashMap).addOnCompleteListener {

                            if (it.isSuccessful) {
                                finish()

                            }
                        }.addOnFailureListener {
                            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    fun gorselsec(view: View) {

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //İzin verilmediyse

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }else {
            //İzin zaten verildiyse

            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent  = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }
        super .onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && requestCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data

            if (secilenGorsel != null) {

                if (Build.VERSION.SDK_INT >= 28 ) {
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    imageview.setImageBitmap(secilenBitmap)
                }else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageview.setImageBitmap(secilenBitmap)

                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}