package com.fcfm.poi.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi.chat.adaptadores.CarreraAdapter
import com.fcfm.poi.chat.modelos.CarrerasF
import com.fcfm.poi.chat.modelos.Mensaje
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*


class ActivitySignup : AppCompatActivity() {
    private val listaCarrerasF = mutableListOf<CarrerasF>()
    private val adaptador = CarreraAdapter(listaCarrerasF)
    private lateinit var mAuth:FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String=""
    lateinit var carreras: Spinner
    private val database = FirebaseDatabase.getInstance()
    private val refSpinner = database.getReference("spinner")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        carreras = findViewById(R.id.spinner)


      // val adapter = ArrayAdapter.createFromResource(this,refSpinner.hashCode(),android.R.layout.simple_spinner_item)
        btn_back.setOnClickListener {
            val intentChat = Intent(this, MainActivity::class.java)
            startActivity(intentChat)
        }

        mAuth = FirebaseAuth.getInstance()
        btn_signup2.setOnClickListener(){
                registerUser()
        }
        loadcarreras()
    }
    public  fun loadcarreras(){
        refSpinner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val carrier: CarrerasF = postSnapshot.getValue(CarrerasF::class.java) as CarrerasF

                    listaCarrerasF.add(carrier)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ActivitySignup, "Error al leer mensajes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun registerUser() {
        val email: String= username2.text.toString()
        val username: String= editTextTextPersonName.text.toString()
        val password: String= password2.text.toString()
        carreras = findViewById(R.id.spinner)

        if(username == ""){
            Toast.makeText(this@ActivitySignup,"please write username", Toast.LENGTH_SHORT).show()
        }else if (email == ""){
            Toast.makeText(this@ActivitySignup,"please write email", Toast.LENGTH_SHORT).show()
        }
        else if (password==""){
            Toast.makeText(this@ActivitySignup,"please write password", Toast.LENGTH_SHORT).show()
        }else   {
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        firebaseUserID= mAuth.currentUser!!.uid
                        refUsers= FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)

                        val userHasMap = HashMap<String,Any>()
                        userHasMap["uid"]= firebaseUserID
                        userHasMap["username"] = username
                        userHasMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/proyectofinalpoi.appspot.com/o/profileicono.png?alt=media&token=1416676e-a829-499e-8d89-02b221318a79"
                        userHasMap["status"]="offline"
                        userHasMap["carrera"]=carreras
                        userHasMap["search"]= username.toLowerCase()

                        refUsers.updateChildren(userHasMap).addOnCompleteListener{
                            task ->
                            if(task.isSuccessful){
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }else{
                        Toast.makeText(this@ActivitySignup,"Error Message:"+ task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}