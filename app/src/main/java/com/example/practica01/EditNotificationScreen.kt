package com.example.practica01

import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.practica01.models.Alarm
import com.example.practica01.objects.State
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import com.example.practica01.objects.AlarmReceiver
import com.example.practica01.objects.Notifications

class EditNotificationScreen : AppCompatActivity() {

    private lateinit var titleTextField: TextInputEditText
    private lateinit var descriptionTextField: TextInputEditText
    private lateinit var selectHourButton: Button
    private lateinit var saveAlarmButton: Button

    private var alarmIndex: Int? = null

    private lateinit var hourText: TextView
    private var hourStr: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_notifications_screen)
        setSupportActionBar(findViewById(R.id.alarms_screen_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        alarmIndex = intent.getIntExtra("AlarmIndex", -1)

        titleTextField = findViewById(R.id.alarm_title_textfield)
        descriptionTextField = findViewById(R.id.alarm_description_textfield)
        selectHourButton = findViewById(R.id.select_hour_button)
        saveAlarmButton = findViewById(R.id.save_alarm_button)
        hourText = findViewById(R.id.hour_label)

        if (alarmIndex != -1) {
            fillForm()
        }

        saveAlarmButton.setOnClickListener {
            if (validateForm()) {
                saveAlarm()
                showNotification()
                Notifications.scheduleAlarm(this)
                finish()
            } else {
                Toast.makeText(this, "Por favor, complete los datos", Toast.LENGTH_SHORT).show()
            }
        }

        selectHourButton.setOnClickListener {
            showTimePicker()
        }
    }

    private fun showNotification() {
        intent = Intent(this, EditNotificationScreen::class.java)
        if(alarmIndex != -1) {
            intent.putExtra("AlarmIndex", alarmIndex)
        }
        else {
            intent.putExtra("AlarmIndex", State.alarms.size - 1)
        }
        Notifications.showNotificationWithIntent(
            this,
            "Alerta guardada",
            "Haga click aqui para editar",
            intent
        )
    }

    private fun fillForm() {
        titleTextField.setText(State.alarms[alarmIndex!!].title)
        descriptionTextField.setText(State.alarms[alarmIndex!!].description)
        hourText.text = State.alarms[alarmIndex!!].hour
        hourStr = State.alarms[alarmIndex!!].hour
    }

    private fun saveAlarm() {
        if (alarmIndex == -1) {
            var newAlarm = Alarm(
                descriptionTextField.text.toString(),
                titleTextField.text.toString(),
                hourStr,
                true
            )
            State.addAlarm(newAlarm)
        } else {
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

    private fun validateForm(): Boolean {
        return !(titleTextField.text.isNullOrBlank() || descriptionTextField.text.isNullOrBlank() || hourStr == "")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    @SuppressLint("SimpleDateFormat")
    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val formattedTime = SimpleDateFormat("HH:mm").format(cal.time)
            setHourLabel(formattedTime)
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setHourLabel(hour: String) {
        hourStr = hour
        hourText.text = hourStr
    }

}