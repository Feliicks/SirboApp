package com.felicksdev.onlymap.data.local.database

import android.content.Context
import androidx.room.Room

class DataBase(private val context:Context) {

    val db = Room.databaseBuilder(
        context.applicationContext,
        SirboDatabase::class.java, "SirboApp"
    ).build()

}