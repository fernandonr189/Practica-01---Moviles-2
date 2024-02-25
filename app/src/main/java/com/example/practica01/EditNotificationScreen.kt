package com.example.practica01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditNotificationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_notifications_screen)
        setSupportActionBar(findViewById(R.id.alarms_screen_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}