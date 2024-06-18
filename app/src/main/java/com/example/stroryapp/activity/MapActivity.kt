package com.example.stroryapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.example.stroryapp.R
import com.example.stroryapp.data.UserRepository
import com.example.stroryapp.data.pref.UserPreference
import com.example.stroryapp.data.pref.dataStore
import com.example.stroryapp.retrofit.ApiConfig
import com.example.stroryapp.retrofit.ApiService
import com.example.stroryapp.viewModel.MapViewModel
import com.example.stroryapp.viewModel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapsViewModel: MapViewModel
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialize ViewModel
        val dataStore: DataStore<Preferences> = this.dataStore
        val apiService = ApiConfig.getApiService()
        val userRepository = UserRepository.getInstance(apiService, UserPreference.getInstance(dataStore))
        mapsViewModel = ViewModelProvider(this, ViewModelFactory(userRepository)).get(MapViewModel::class.java)

        // Initialize the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        observeViewModel()
    }

    private fun observeViewModel() {
        mapsViewModel.getStoriesWithLocation().observe(this, { result ->
            when (result) {
                is com.example.stroryapp.data.Result.Loading -> {
                    // Show loading state
                }
                is com.example.stroryapp.data.Result.Success -> {
                    // Handle success
                    val stories = result.data.listStory
                    for (story in stories) {
                        val location = LatLng(story.lat as Double, story.lon as Double)
                        map.addMarker(MarkerOptions().position(location).title(story.name))
                    }
                }
                is com.example.stroryapp.data.Result.Error -> {
                    // Handle error
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}