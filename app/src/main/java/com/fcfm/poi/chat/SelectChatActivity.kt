package com.fcfm.poi.chat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.fcfm.poi.chat.adaptadores.ChatSelectionAdapter
import com.fcfm.poi.chat.modelos.Chat
import com.fcfm.poi.chat.modelos.MensajeP
import com.fcfm.poi.chat.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_select_chat.*

class SelectChatActivity : AppCompatActivity() {

    private val listaChats = mutableListOf<Chat>()
    private val adaptador = ChatSelectionAdapter(listaChats)

    private val database = FirebaseDatabase.getInstance()
    private val chatRefP = database.getReference("chats/P")//Chat privado
    private val chatRefG = database.getReference("chats/G")//Chat grupal
    private val usersRef = database.getReference("Users") //Pues... A usuarios vea (No se esta usando)

    private val mAuth = FirebaseAuth.getInstance()

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups_window)
        val crearChat = findViewById<Button>(R.id.btnCrear)
        val etCrearChat = findViewById<EditText>(R.id.textView6)



        recibirTodosLosChats(buscarUserPathPorUID(mAuth.currentUser!!.uid))

        crearChat.setOnClickListener {
            if (etCrearChat.text.toString().equals("")) {
                Toast.makeText(
                    this@SelectChatActivity,
                    "Por favor seleccione el contacto",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                if(buscarUsuarioUID(etCrearChat.text.toString()).equals("")) //Validar que exista el usuario
                {
                    Toast.makeText(this@SelectChatActivity,"Contacto inexistente", Toast.LENGTH_LONG).show()
                }
                else
                {
                    crearChatPrivado(etCrearChat.text.toString())

                    val intentChat = Intent(this, ChatActivityPrivate::class.java)

                    startActivity(intentChat)
                }
            }
        }
    }

    private fun buscarUsuarioUID(inputUser: String): String {
        val searchRef = database.getReference("Users")
        val listaUsers = mutableListOf<Usuario>()
        var answer = ""
        searchRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //listaMensajesP.clear()

                for (snap in snapshot.children) {

                    //val user: Usuario = snap.getValue(Usuario::class.java) as Usuario
                    val usuario: Usuario = snap.getValue(Usuario::class.java) as Usuario
                    if (usuario.username.equals(inputUser)) {
                        answer = usuario.uid
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@SelectChatActivity,
                    "Error al leer usuarios",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return answer
    }

    private fun buscarUserPathPorUID(inputUID : String) :String {
        val searchRef = database.getReference("Users")
        val listaUsers = mutableListOf<Usuario>()
        var answer = ""
        searchRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                //listaMensajesP.clear()

                for (snap in snapshot.children) {

                    //val user: Usuario = snap.getValue(Usuario::class.java) as Usuario
                    val usuario: Usuario = snap.getValue(Usuario::class.java) as Usuario
                    if (usuario.uid.equals(inputUID)) {
                        answer = snap.ref.toString()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@SelectChatActivity,
                    "Error al leer usuarios",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return answer
    }

    private fun crearChatPrivado(destinatario: String): String {

        var idChat = ""

        //Obtener los UID de los usuarios
        val currentUserUID = mAuth.currentUser!!.uid
        val otherUserUID = buscarUsuarioUID(destinatario)

        if (otherUserUID.equals("")) {
            //Crear el chat
            val chatFirebase = chatRefP.push()
            idChat = chatFirebase.key ?: ""

            //Referencia a donde se alocaran los IDs de los usuarios
            val createdChatUsers = database.getReference("chatsP/$idChat/Users")
            //Asignar los UID
            createdChatUsers.setValue(currentUserUID)//Usuario loggeado
            createdChatUsers.setValue(buscarUsuarioUID(otherUserUID))//El otro usuario

            //Pasarle el ID del chat a los usuarios
            val remitentePathRef = database.getReference("${buscarUserPathPorUID(currentUserUID)}/Chats")
            val destinatarioPathRef = database.getReference("${buscarUserPathPorUID(otherUserUID)}/Chats")
            remitentePathRef.setValue(idChat)
            destinatarioPathRef.setValue(idChat)
        } else {
            Toast.makeText(this@SelectChatActivity, "Usuario invalido", Toast.LENGTH_LONG).show()
        }
        //val newChat = Chat(idChat, "...", currentUserUID, "", "")
        //chatFirebase.setValue(newChat)

        return idChat
    }

    private fun recibirTodosLosChats(userPath : String) {
        //Buscar al usuario, y por cada uno de sus ID de chat -> recibirChatPorID(id) y llenar la lista
        val userChatsRef = database.getReference("$userPath/Chats")
        userChatsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                listaChats.clear()

                for (snap in snapshot.children) {

                    val idChat: String = snap.getValue(String::class.java) as String
                    listaChats.add(recibirChatPorID(idChat))

                }

                adaptador.notifyDataSetChanged()
                if (listaChats.size > 0) {
                    rvChat.smoothScrollToPosition(listaChats.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@SelectChatActivity,
                    "Error al leer chats",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun recibirChatPorID(idChat:String) : Chat {
        lateinit var answer : Chat
        val userChatsRef = database.getReference("chats/P")
        userChatsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {

                    val id: String = snap.getValue(String::class.java) as String //Corroborar esta validacion, no confio en recibirlo como string
                    if(id.equals(idChat)) //Validar que sea el chat que estamos buscando
                    {
                        val answerChat: Chat = snap.getValue(Chat::class.java) as Chat
                        answer = answerChat
                    }

                }
                adaptador.notifyDataSetChanged()
                if (listaChats.size > 0) {
                    rvChat.smoothScrollToPosition(listaChats.size - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@SelectChatActivity,
                    "Error al leer mensajes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return answer
    }
}