package com.example.practica01.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practica01.R
import com.example.practica01.models.Alarm
import com.example.practica01.models.ContextMenuCallback
import com.example.practica01.objects.State
import com.google.android.material.switchmaterial.SwitchMaterial

class NotificationScreenRecyclerAdapter(alarms : MutableList<Alarm>, context : Context, cMenu : ContextMenuCallback) : RecyclerView.Adapter<NotificationScreenRecyclerAdapter.ViewHolder>() {

    private val _alarms : MutableList<Alarm> = alarms
    private val _context : Context = context
    private val _cMenu : ContextMenuCallback = cMenu

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationScreenRecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycler_item, parent, false))
    }
    override fun onBindViewHolder(
        holder: NotificationScreenRecyclerAdapter.ViewHolder,
        position: Int
    ) {
        val item = this._alarms[position]
        holder.bind(item, _context)
        holder.itemView.setOnClickListener {
            val popup = PopupMenu(_context, holder.itemView)
            popup.inflate(R.menu.note_item_menu)
            popup.setOnMenuItemClickListener {
                _cMenu.onLongClick(it, position)
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int {
        return this._alarms.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val alarmTitle = view.findViewById(R.id.item_title) as TextView
        private val alarmDescription = view.findViewById(R.id.item_description) as TextView
        private val isEnabledSwitch = view.findViewById(R.id.switch1) as SwitchMaterial

        fun bind(alarm : Alarm, context : Context) {
            alarmTitle.text = alarm.title
            alarmDescription.text = alarm.description
            isEnabledSwitch.isChecked = alarm.isActive
            isEnabledSwitch.setOnClickListener {
                alarm.isActive = isEnabledSwitch.isChecked
                val json = State.toJson()
                val sharedpref = context.getSharedPreferences("State", Context.MODE_PRIVATE)
                with(sharedpref.edit()) {
                    putString("State", json)
                    apply()
                }
            }
        }
    }
}