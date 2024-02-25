package com.example.practica01.models

import android.view.MenuItem

interface ContextMenuCallback {
    fun onLongClick(menuItem : MenuItem, position: Int) : Boolean
}