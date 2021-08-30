package com.fcfm.poi.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        findViewById<Button>(R.id.btn_signup).setOnClickListener{
            val mainIntent = Intent(this, ActivitySignup::class.java)
            startActivity(mainIntent)
        }

    }

}