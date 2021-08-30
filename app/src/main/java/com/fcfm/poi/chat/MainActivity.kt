package com.fcfm.poi.chat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            loginUser()
        }
        btn_signup.setOnClickListener {
                val intentChat = Intent(this, ActivitySignup::class.java)
                startActivity(intentChat)
        }
    }

    private fun loginUser() {
        val email: String= username.text.toString()
        val password: String= password.text.toString()
        if (email == ""){
            Toast.makeText(this@MainActivity,"please write email", Toast.LENGTH_SHORT).show()
        }
        else if (password==""){
            Toast.makeText(this@MainActivity,"please write password", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if (task.isSuccessful){
                        val intent = Intent(this, SelectChatActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this@MainActivity, "ErrorMessage" + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


}