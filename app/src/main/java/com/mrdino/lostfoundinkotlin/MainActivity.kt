package com.mrdino.lostfoundinkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createAdvertButton: Button = findViewById(R.id.createAdvertButton)
        val showAllItemsButton: Button = findViewById(R.id.showAllItemsButton)
        val showMapButton: Button = findViewById(R.id.showMapButton)

        createAdvertButton.setOnClickListener {
            val intent = Intent(this, CreateAdvertActivity::class.java)
            startActivity(intent)
        }

        showAllItemsButton.setOnClickListener {
            val intent = Intent(this, ShowItemsActivity::class.java)
            startActivity(intent)
        }

        showMapButton.setOnClickListener {
            showMapButtonClicked()
        }
    }

    private fun showMapButtonClicked() {
        val itemsWithLocations = getItemsFromDatabase()

        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra("itemsWithLocations", ArrayList(itemsWithLocations))
        startActivity(intent)
    }

    private fun getItemsFromDatabase(): List<Item> {
        val dbHelper = DatabaseHelper(this)
        return dbHelper.getAllItems()
    }
}
