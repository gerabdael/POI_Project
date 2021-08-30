package com.fcfm.poi.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.fcfm.poi.chat.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_groups_window.*

class Activity_groups_window : AppCompatActivity() {

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups_window)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers= FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)

        findViewById<Button>(R.id.btnCrear).setOnClickListener{
            val mainIntent = Intent(this, SelectChatActivity::class.java)
            startActivity(mainIntent)
        }

    }
}