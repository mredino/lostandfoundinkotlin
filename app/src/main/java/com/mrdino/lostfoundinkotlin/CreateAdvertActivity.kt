package com.mrdino.lostfoundinkotlin

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import java.io.IOException
import java.util.Locale

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var advertTypeRadioGroup: RadioGroup
    private lateinit var nameEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var goBackButton: Button

    private lateinit var placesClient: PlacesClient
    private lateinit var locationAutocomplete: AutoCompleteTextView
    private lateinit var getCurrentLocationButton: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        advertTypeRadioGroup = findViewById(R.id.advertTypeRadioGroup)
        nameEditText = findViewById(R.id.nameEditText)
        contactEditText = findViewById(R.id.contactEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        dateEditText = findViewById(R.id.dateEditText)
        saveButton = findViewById(R.id.saveButton)
        goBackButton = findViewById(R.id.goBackButton)

        // Initialise Places SDK
        Places.initialize(applicationContext, apiKey)
        placesClient = Places.createClient(this)

        // Attach AutoCompleteTextView
        locationAutocomplete = findViewById(R.id.locationAutocomplete)
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton)

        // Create AutoCompleteSessionToken
        val token = AutocompleteSessionToken.newInstance()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up the Autocomplete feature for location
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            listOf("Location 1", "Location 2", "Location 3")
        )
        locationAutocomplete.setAdapter(adapter)

        // Set AutoComplete Query listener
        locationAutocomplete.setOnItemClickListener { _, _, position, _ ->
            val selectedPlace = adapter.getItem(position)
            locationAutocomplete.setText(selectedPlace)
            if (selectedPlace != null) {
                updateLocationCoordinates(selectedPlace)
            }
        }

        locationAutocomplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Fetch autocomplete predictions
                val request = FindAutocompletePredictionsRequest.builder()
                    .setQuery(s.toString())
                    .setSessionToken(token)
                    .build()

                placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener { response ->
                        adapter.clear()
                        for (prediction in response.autocompletePredictions) {
                            adapter.add(prediction.getFullText(null).toString())
                        }
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener { exception ->
                        // Handle error
                    }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        // Set up the click listener for the "Get Current Location" button
        getCurrentLocationButton.setOnClickListener {
            getCurrentLocation()
        }

        saveButton.setOnClickListener {
            saveAdvert()
        }

        goBackButton.setOnClickListener {
            finish()
        }
    }

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
        val location = locationAutocomplete.text.toString()

        val item = Item(0, advertType, name, contact, description, date, location, latitude, longitude)
        val dbHelper = DatabaseHelper(this)
        dbHelper.insertItem(item)

        finish()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Retrieve the user's current location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val address = getAddressFromLocation(currentLatLng)
                locationAutocomplete.setText(address)
                updateLocationCoordinates(address)
            } ?: run {
                Toast.makeText(this, "Failed to retrieve location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getAddressFromLocation(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addressText = ""
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address = addresses?.get(0)
                    if (address != null) {
                        addressText = address.getAddressLine(0)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressText
    }

    private fun updateLocationCoordinates(address: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val addressLocation = addresses?.get(0)
                    if (addressLocation != null) {
                        latitude = addressLocation.latitude
                    }
                    if (addressLocation != null) {
                        longitude = addressLocation.longitude
                    }
                } else {
                    latitude = 0.0
                    longitude = 0.0
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            latitude = 0.0
            longitude = 0.0
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val apiKey = "Get your own Google Maps API key" // Replace with your own API key
    }
}
