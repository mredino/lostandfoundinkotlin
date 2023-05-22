package com.mrdino.lostfoundinkotlin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "lost_found_db"
        private const val DATABASE_VERSION = 1

        // Define the table name and column names
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CONTACT = "contact"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_LOCATION = "location"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the database table
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TYPE TEXT, $COLUMN_NAME TEXT, $COLUMN_CONTACT TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, $COLUMN_DATE TEXT, $COLUMN_LOCATION TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertItem(item: Item) {
        val db = writableDatabase

        val contentValues = ContentValues().apply {
            put(COLUMN_TYPE, item.type)
            put(COLUMN_NAME, item.name)
            put(COLUMN_CONTACT, item.contact)
            put(COLUMN_DESCRIPTION, item.description)
            put(COLUMN_DATE, item.date)
            put(COLUMN_LOCATION, item.location)
        }

        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun getAllItems(): ArrayList<Item> {
        val itemList = ArrayList<Item>()
        val db = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val contact = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION))

                val item = Item(id, type, name, contact, description, date, location)
                itemList.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }

    fun deleteItem(item: Item) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(item.id.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}
