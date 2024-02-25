package com.example.practica01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica01.adapters.NotificationScreenRecyclerAdapter
import com.example.practica01.models.Alarm

class NotificationsScreen : AppCompatActivity() {
    
    private lateinit var fabButton : View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter : NotificationScreenRecyclerAdapter
    private lateinit var alarmList : ArrayList<Alarm>
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_screen)

        alarmList = ArrayList()
        alarmList.add(Alarm("Henlo", "henlo description", "osid", false))
        alarmList.add(Alarm("Henlo", "henlo description", "osid", true))

        
        fabButton = findViewById(R.id.floatingActionButton)
        fabButton.setOnClickListener {
            var intent = Intent(this, EditNotificationScreen::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.alarms_recycler)
        recyclerAdapter = NotificationScreenRecyclerAdapter(alarmList, this)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}