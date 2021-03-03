package com.muhammed.fotografpaylasmakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val guncelKullanici = auth.currentUser

        if  (guncelKullanici != null) {
            val intent = Intent(applicationContext,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    fun girisYap(view: View) {

        val email = emailedittext.text.toString()
        val sifre = passedittext.text.toString()
        auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener {
            if (it.isSuccessful) {

                Toast.makeText(applicationContext, "HoşGeldin ${auth.currentUser.email.toString()}", Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)
                finish()

            }
        }.addOnFailureListener {
            if (it != null) {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
    fun kayıtet(view: View) {
        val email = emailedittext.text.toString()
        val sifre = passedittext.text.toString()
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(applicationContext, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            if (it!= null) {
                Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

}