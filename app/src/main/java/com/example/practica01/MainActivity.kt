package com.example.practica01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton : Button
    private lateinit var exitButton : Button

    private lateinit var userTextField : TextInputEditText
    private lateinit var passwordTextField : TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.login_button)
        exitButton = findViewById(R.id.exit_button)
        userTextField = findViewById(R.id.username_text_field)
        passwordTextField = findViewById(R.id.password_text_field)

        loginButton.setOnClickListener {
            if(formIsValid()) {
                doLogin()
            }
        }
    }

    private fun formIsValid() : Boolean {
        return !(userTextField.text.isNullOrBlank() || passwordTextField.text.isNullOrBlank())
    }

    private fun doLogin() {
        val intent = Intent(this, NotificationsScreen::class.java)
        startActivity(intent)
    }
}