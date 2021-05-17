package com.fcfm.poi.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi.chat.adaptadores.ChatAdapterP
import com.fcfm.poi.chat.modelos.MensajeP
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivityPrivate : AppCompatActivity() {

    private val listaMensajesP = mutableListOf<MensajeP>()
    private val adaptador = ChatAdapterP(listaMensajesP)
    private lateinit var remitente: String
    private lateinit var destinatario: String

    private lateinit var idChat: String

    private val database = FirebaseDatabase.getInstance()
    private val chatRefP = database.getReference("chatsP")

    private lateinit var thisChatRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        remitente = intent.getStringExtra("nombreUsuario") ?: "Anónimo"
        destinatario = intent.getStringExtra("nombreUsuario2") ?: "Anónimo"

        //Obtener la llave del chat
        /*idChat = */ if (remitente < destinatario) {
            thisChatRef = database.getReference("chatsP/$remitente$destinatario")
        } else {
            thisChatRef = database.getReference("chatsP/$destinatario$remitente")
        }

        rvChat.adapter = adaptador

        btnEnviar.setOnClickListener {

            val mensajep = txtMensaje.text.toString()
            if (mensajep.isNotEmpty()) {

                txtMensaje.text.clear()
                enviarMensaje(
                    MensajeP(
                        "",
                        "",
                        mensajep,
                        remitente,
                        destinatario,
                        ServerValue.TIMESTAMP
                    )
                )
            }
        }

        recibirMensajes()
    }


    private fun enviarMensaje(mensajep: MensajeP) {

        //val mensajeFirebase = chatRefP.push()
        val mensajeFirebase = thisChatRef.push()
        mensajep.id = mensajeFirebase.key ?: ""
        mensajep.id2 = mensajeFirebase.key ?: ""

        mensajeFirebase.setValue(mensajep)
    }


    private fun recibirMensajes() {


        //chatRefP.addValueEventListener(object : ValueEventListener {
        thisChatRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listaMensajesP.clear()

                for (snap in snapshot.children) {

                    val mensajep: MensajeP = snap.getValue(MensajeP::class.java) as MensajeP
                    if (mensajep.de == remitente)
                        mensajep.esMio = true

                    listaMensajesP.add(mensajep)
                }

                adaptador.notifyDataSetChanged()
                if (listaMensajesP.size > 0) {
                    rvChat.smoothScrollToPosition(listaMensajesP.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ChatActivityPrivate,
                    "Error al leer mensajes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
