package com.example.practica01

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.practica01.objects.State
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
                if(userTextField.text.toString() == "fernando" && passwordTextField.text.toString() == "33656340") {
                    doLogin()
                }
                else {
                    Toast.makeText(this, "Nombre o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        var sharedPref = this.getSharedPreferences("State", Context.MODE_PRIVATE)
        var StateJson = sharedPref.getString("State", State.toJson())
        State.fromJson(StateJson!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notis :)"
            val descriptionText = "Notis :)"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel("Notis :)", name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
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