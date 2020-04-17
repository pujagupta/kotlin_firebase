package com.example.my22application.kotlinpoc.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.my22application.kotlinpoc.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewAccountActivity : AppCompatActivity() {
    private var etemailId: EditText? = null
    private var etpassword: EditText? = null
    private var btn_register: Button? = null
    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private val TAG = "Newaccount"
    //global variables
    private var emailId: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)
        initialise()
    }

    private fun initialise() {
        etemailId = findViewById<View>(R.id.emailId) as EditText
        etpassword = findViewById<View>(R.id.password) as EditText
        btn_register = findViewById<View>(R.id.btn_register) as Button
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        btn_register!!.setOnClickListener { createNewAccount() }
    }

    private fun createNewAccount() {
        emailId = etemailId?.text.toString()
        password = etpassword?.text.toString()
        if (!TextUtils.isEmpty(emailId) && !TextUtils.isEmpty(password)) {
            mAuth!!
                .createUserWithEmailAndPassword(emailId!!, password!!)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "created.",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(
                            Intent(this,
                                LoginActivity::class.java)
                        )
                        etemailId?.text?.clear()
                        etpassword?.text?.clear()
                    } else {
                        Toast.makeText(
                            this, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
}