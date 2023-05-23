package com.mrdino.lostfoundinkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var itemsWithLocations: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        itemsWithLocations = intent.getSerializableExtra("itemsWithLocations") as ArrayList<Item>

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add markers for the lost and found item locations
        for (item in itemsWithLocations) {
            val latLng = LatLng(item.latitude, item.longitude)
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title(item.name)
                .snippet(item.description)
            mMap.addMarker(markerOptions)
        }

        // Move the camera to the first item location
        if (itemsWithLocations.isNotEmpty()) {
            val firstItemLatLng = LatLng(itemsWithLocations[0].latitude, itemsWithLocations[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLatLng, 12f))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
