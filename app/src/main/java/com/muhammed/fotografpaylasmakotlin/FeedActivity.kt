package com.muhammed.fotografpaylasmakotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_feed.*
import java.util.ArrayList

class FeedActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    var postlist  =  ArrayList<Post>()
    private lateinit var recylerViewAdapter :FeedRecylerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        recylerview.layoutManager = layoutManager
        recylerViewAdapter = FeedRecylerAdapter(postlist)
        recylerview.adapter = recylerViewAdapter
    }

    private fun verileriAl() {


        db.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->

            if  (exception != null ){

                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else {

                if (snapshot != null) {
                    if (snapshot.isEmpty) {

                        val documents = snapshot.documents
                        postlist.clear()

                        for (document in documents) {

                            val kullanıcıEmail  = document.get("kullanıcıemail") as String
                            val kullanıcıYorum = document.get("kullanıcıyorum") as String
                            val gorselUrl = document.get("gorselUrl") as String
                            val timestamp = document.get("tarih") as Timestamp
                            val tarih = timestamp.toDate()

                            val indirilenPost = Post(kullanıcıEmail,kullanıcıYorum,gorselUrl)
                            postlist.add(indirilenPost)

                        }
                        recylerViewAdapter.notifyDataSetChanged()
                    }

                }

            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if  (item.itemId == R.id.fotograf_ekle) {
            val intent = Intent(applicationContext,FotografActivity::class.java)
            startActivity(intent)

        }else if (item.itemId == R.id.cikis_yap) {

            auth.signOut()
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}