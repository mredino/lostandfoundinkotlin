package com.mrdino.lostfoundinkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var advertTypeRadioGroup: RadioGroup
    private lateinit var nameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var goBackButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        advertTypeRadioGroup = findViewById(R.id.advertTypeRadioGroup)
        nameEditText = findViewById(R.id.nameEditText)
        contactEditText = findViewById(R.id.contactEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        dateEditText = findViewById(R.id.dateEditText)
        locationEditText = findViewById(R.id.locationEditText)
        saveButton = findViewById(R.id.saveButton)
        goBackButton = findViewById(R.id.goBackButton)

        saveButton.setOnClickListener {
            saveAdvert()
        }

        goBackButton.setOnClickListener {
            finish()
        }
    }

    // Inside the saveAdvert() function
    private fun saveAdvert() {
        val advertType = when (advertTypeRadioGroup.checkedRadioButtonId) {
            R.id.lostRadioButton -> "Lost"
            R.id.foundRadioButton -> "Found"
            else -> ""
        }

        val name = nameEditText.text.toString()
        val contact = contactEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val date = dateEditText.text.toString()
        val location = locationEditText.text.toString()

        val item = Item(0, advertType, name, contact, description, date, location)
        val dbHelper = DatabaseHelper(this)
        dbHelper.insertItem(item)

        finish()
    }

}
