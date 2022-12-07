package com.example.storyappsubmission1.Activity.Main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.example.storyappsubmission1.Data.Functon.Preference
import com.example.storyappsubmission1.Data.Response.ListStoryR
import com.example.storyappsubmission1.R
import com.example.storyappsubmission1.ViewModel.MapsVM
import com.example.storyappsubmission1.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var _binding : ActivityMapsBinding? = null
    private val binding get() = _binding!!

    private val mapsViewModel : MapsVM by viewModels()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preference")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = Preference.getInstance(dataStore)

        preferences.getToken().asLiveData().observe(this){
            mapsViewModel.getLocationStory(it)
        }


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.storyWithLocation.observe(this){
            createMarkerStory(it, mMap)
        }

        getMyLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun createMarkerStory(listStory: List<ListStoryR>, mMap: GoogleMap){
        for (story in listStory){
            val marker = LatLng(story.lat ?: 0.0, story.lon ?: 0.0)
            mMap.addMarker(MarkerOptions().position(marker).title("${story.name} Stories"))
        }
    }

    private fun getMyLocation(){
        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            mMap.cameraPosition
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}