package com.example.practica01

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.practica01.models.Alarm
import com.example.practica01.objects.State
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import android.Manifest
import android.content.pm.PackageManager

class EditNotificationScreen : AppCompatActivity() {

    private lateinit var titleTextField : TextInputEditText
    private lateinit var descriptionTextField: TextInputEditText
    private lateinit var selectHourButton : Button
    private lateinit var saveAlarmButton: Button

    private var alarmIndex : Int? = null

    private lateinit var hourText : TextView
    private var hourStr : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_notifications_screen)
        setSupportActionBar(findViewById(R.id.alarms_screen_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        showNotification()

        alarmIndex = intent.getIntExtra("AlarmIndex", -1)

        titleTextField = findViewById(R.id.alarm_title_textfield)
        descriptionTextField = findViewById(R.id.alarm_description_textfield)
        selectHourButton = findViewById(R.id.select_hour_button)
        saveAlarmButton = findViewById(R.id.save_alarm_button)
        hourText = findViewById(R.id.hour_label)

        if(alarmIndex != -1) {
            fillForm()
        }

        saveAlarmButton.setOnClickListener {
            if(validateForm()) {
                saveAlarm()
                finish()
            }
            else {
                Toast.makeText(this, "Por favor, complete los datos", Toast.LENGTH_SHORT).show()
            }
        }

        selectHourButton.setOnClickListener {
            showTimePicker()
        }
    }

    private fun fillForm() {
        titleTextField.setText(State.alarms[alarmIndex!!].title)
        descriptionTextField.setText(State.alarms[alarmIndex!!].description)
        hourText.text = State.alarms[alarmIndex!!].hour
        hourStr = State.alarms[alarmIndex!!].hour
    }

    private fun saveAlarm() {
        if(alarmIndex == -1) {
            var newAlarm = Alarm(descriptionTextField.text.toString(), titleTextField.text.toString(), hourStr, true)
            State.addAlarm(newAlarm)
        }
        else {
            State.alarms[alarmIndex!!].title = titleTextField.text.toString()
            State.alarms[alarmIndex!!].description = descriptionTextField.text.toString()
            State.alarms[alarmIndex!!].hour = hourStr
        }

        val json = State.toJson()
        val sharedpref = this.getSharedPreferences("State", Context.MODE_PRIVATE)
        with(sharedpref.edit()) {
            putString("State", json)
            apply()
        }
    }
    private fun validateForm() : Boolean {
        return !(titleTextField.text.isNullOrBlank() || descriptionTextField.text.isNullOrBlank() || hourStr == "")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val formattedTime = SimpleDateFormat("HH:mm").format(cal.time)
            setHourLabel(formattedTime)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun setHourLabel(hour : String){
        hourStr = hour
        hourText.text = hourStr
    }
    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = "Notis :)"
            val descriptionText = "Notis :)"
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel("Notis :)", name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }


        var builder = NotificationCompat.Builder(this, "Notis :)")
            .setSmallIcon(androidx.core.R.drawable.notification_bg)
            .setContentTitle("Alarma agregada exitosamente")
            .setPriority(NotificationCompat.PRIORITY_LOW)

        with(NotificationManagerCompat.from(this)) {
            if(ActivityCompat.checkSelfPermission(
                    this@EditNotificationScreen,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(1, builder.build())
        }
    }
}