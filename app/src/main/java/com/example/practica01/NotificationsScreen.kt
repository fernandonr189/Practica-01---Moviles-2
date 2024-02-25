package com.example.practica01

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica01.adapters.NotificationScreenRecyclerAdapter
import com.example.practica01.models.Alarm
import com.example.practica01.models.ContextMenuCallback
import com.example.practica01.objects.State

class NotificationsScreen : AppCompatActivity(), ContextMenuCallback{
    
    private lateinit var fabButton : View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter : NotificationScreenRecyclerAdapter
    private lateinit var alarmList : ArrayList<Alarm>
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_screen)
        alarmList = State.alarms
        
        fabButton = findViewById(R.id.floatingActionButton)
        fabButton.setOnClickListener {
            var intent = Intent(this, EditNotificationScreen::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.alarms_recycler)
        recyclerAdapter = NotificationScreenRecyclerAdapter(alarmList, this, this)

        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recyclerAdapter.notifyDataSetChanged()
    }

    private fun showAlertDialog(position : Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Advertencia")
            .setMessage("¿Está seguro de que desea eliminar esta alarma?")
            .setNegativeButton("Cancelar") { view, _ ->
                Toast.makeText(this, "Accion cancelada", Toast.LENGTH_SHORT).show()
                view.dismiss()
            }
            .setPositiveButton("Aceptar") { view, _ ->
                deleteAlarm(position)
                Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                view.dismiss()
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    private fun deleteAlarm(position: Int) {
        State.alarms.removeAt(position)
        recyclerAdapter.notifyItemRemoved(position)
        val json = State.toJson()
        val sharedpref = this.getSharedPreferences("State", Context.MODE_PRIVATE)
        with(sharedpref.edit()) {
            putString("State", json)
            apply()
        }
    }

    override fun onLongClick(menuItem: MenuItem, position : Int) : Boolean {
        when(menuItem.itemId) {
            R.id.edit_menu_item -> {
                val intent = Intent(this, EditNotificationScreen::class.java)
                intent.putExtra("NoteIndex", position)
                startActivity(intent)
            }
            R.id.delete_menu_item -> {
                showAlertDialog(position)
            }
        }
        return true
    }
}