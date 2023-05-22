package com.mrdino.lostfoundinkotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Item(
    val id: Long,
    val type: String,
    val name: String,
    val contact: String,
    val description: String,
    val date: String,
    val location: String
)
