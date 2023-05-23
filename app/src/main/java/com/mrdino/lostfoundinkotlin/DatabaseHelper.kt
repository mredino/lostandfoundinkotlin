package com.mrdino.lostfoundinkotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "lost_found_db"
        private const val DATABASE_VERSION = 1

        // Define the table name and column names
        private const val TABLE_LOST_FOUND = "items"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CONTACT = "contact"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_LOST_FOUND (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TYPE TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_CONTACT TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_DATE TEXT NOT NULL,
                $COLUMN_LOCATION TEXT NOT NULL,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades if needed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOST_FOUND")
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
            put(COLUMN_LATITUDE, item.latitude) // Insert latitude value
            put(COLUMN_LONGITUDE, item.longitude) // Insert longitude value
        }

        db.insert(TABLE_LOST_FOUND, null, contentValues)
        db.close()
    }

    fun getAllItems(): MutableList<Item> {
        val itemList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_LOST_FOUND ORDER BY $COLUMN_ID DESC", null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID))
                    val advertType = it.getString(it.getColumnIndexOrThrow(COLUMN_TYPE))
                    val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                    val contact = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTACT))
                    val description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    val date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE))
                    val location = it.getString(it.getColumnIndexOrThrow(COLUMN_LOCATION))
                    val latitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUDE))
                    val longitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                    val item = Item(id, advertType, name, contact, description, date, location, latitude, longitude)
                    itemList.add(item)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return itemList
    }

    fun deleteItem(item: Item) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(item.id.toString())
        db.delete(TABLE_LOST_FOUND, whereClause, whereArgs)
        db.close()
    }
}
