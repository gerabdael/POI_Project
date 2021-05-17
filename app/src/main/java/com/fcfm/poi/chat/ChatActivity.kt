package com.fcfm.poi.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi.chat.adaptadores.ChatAdapter
import com.fcfm.poi.chat.modelos.Mensaje
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    private val listaMensajes = mutableListOf<Mensaje>()
    private val adaptador = ChatAdapter(listaMensajes)
    private lateinit var nombreUsuario: String

    private val database = FirebaseDatabase.getInstance()
    private val chatRef = database.getReference("chats")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Anónimo"

        rvChat.adapter = adaptador

        btnEnviar.setOnClickListener {

            val mensaje = txtMensaje.text.toString()
            if (mensaje.isNotEmpty()) {

                txtMensaje.text.clear()
                enviarMensaje(Mensaje("", mensaje, nombreUsuario, ServerValue.TIMESTAMP))
            }
        }

        recibirMensajes()
    }

    private fun enviarMensaje(mensaje: Mensaje) {

        val mensajeFirebase = chatRef.push()
        mensaje.id = mensajeFirebase.key ?: ""

        mensajeFirebase.setValue(mensaje)
    }

    private fun recibirMensajes() {


        chatRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listaMensajes.clear()

                for (snap in snapshot.children) {

                    val mensaje: Mensaje = snap.getValue(Mensaje::class.java) as Mensaje
                    if (mensaje.de == nombreUsuario)
                        mensaje.esMio = true

                    listaMensajes.add(mensaje)
                }

                adaptador.notifyDataSetChanged()
                if (listaMensajes.size > 0) {
                    rvChat.smoothScrollToPosition(listaMensajes.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@ChatActivity, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}