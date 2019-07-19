package com.example.windy

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    //Firebase references
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAuth: FirebaseAuth? = null

    private val TAG = "MainActivity"
    //global variables
    private var email: String? = null
    private var password: String? = null
    //UI elements
    private var tvForgotPassword: TextView? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null
    private var mProgressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //obtain firebase analytics instance
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        initialise()
    }


    private fun initialise() {
        tvForgotPassword = findViewById<View>(R.id.fgtPass) as TextView
        etEmail = findViewById<View>(R.id.regEmailTxt) as EditText
        etPassword = findViewById<View>(R.id.passTxt) as EditText
        btnLogin = findViewById<View>(R.id.logBut) as Button
        btnCreateAccount = findViewById<View>(R.id.accountBut) as Button
        mProgressBar = ProgressDialog(this)
        mAuth = FirebaseAuth.getInstance()
        tvForgotPassword!!
            .setOnClickListener { startActivity(
                Intent(this@MainActivity,
                    ResetActivity::class.java)
            ) }
        btnCreateAccount!!
            .setOnClickListener { startActivity(
                Intent(this@MainActivity,
                    RegisterActivity::class.java)
            ) }
        btnLogin!!.setOnClickListener { loginUser() }
    }

    //Defn the login User func.
    private fun loginUser() {
        email = etEmail?.text.toString()
        password = etPassword?.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar!!.setMessage("Logging in User ...")
            mProgressBar!!.show()
            Log.d(TAG, "Logging in user.")
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    mProgressBar!!.hide()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        updateUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@MainActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    //And once the user has successfully logged in, they will be taken to the MainActivity
    private fun updateUI() {
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
