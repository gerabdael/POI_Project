package com.fcfm.poi.chat

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fcfm.poi.chat.modelos.CarrerasF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.listOf
import kotlin.collections.mutableListOf
import kotlin.collections.set


class ActivitySignup : AppCompatActivity() {
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


        val lista = listOf("LMAD","LCC","FISICA","Matematicas")
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,lista)
        carreras.adapter = adaptador

        btn_back.setOnClickListener {
            val intentChat = Intent(this, MainActivity::class.java)
            startActivity(intentChat)
        }

        mAuth = FirebaseAuth.getInstance()
        btn_signup2.setOnClickListener {
                registerUser()
        }
    }

    private fun registerUser() {
        val email: String= username2.text.toString()
        val username: String= editTextTextPersonName.text.toString()
        val password: String= password2.text.toString()


        if(username == ""){
            Toast.makeText(this@ActivitySignup,"please write username", Toast.LENGTH_SHORT).show()
        }else if (email == ""){
            Toast.makeText(this@ActivitySignup,"please write email", Toast.LENGTH_SHORT).show()
        } //Validar si no hay campos vacios y notificarlo
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
                        userHasMap["carrera"]="LMAD"
                        userHasMap["search"] = username.toLowerCase(Locale.getDefault())

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