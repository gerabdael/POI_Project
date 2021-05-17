package com.fcfm.poi.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_select_chat.*

class SelectChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_chat)

        val continueButton = findViewById<Button>(R.id.btnContinue)
        val editTextSelectUser = findViewById<EditText>(R.id.etSelectChat)

        continueButton.setOnClickListener {

            val remitente = intent.getStringExtra("nombreUsuario") ?: "Anónimo"
            val destinatario = etSelectChat.text.toString()

            val intentChat = Intent(this, ChatActivityPrivate::class.java)
            intentChat.putExtra("nombreUsuario", remitente)
            intentChat.putExtra("nombreUsuario2", destinatario)

            startActivity(intentChat)
        }
    }
}