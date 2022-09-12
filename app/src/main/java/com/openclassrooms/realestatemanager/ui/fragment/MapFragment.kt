package com.openclassrooms.realestatemanager.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MapFragment : BaseFragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var binding: FragmentMapBinding

    private lateinit var viewModel: MapViewModel

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewPager: ViewPager2

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = ViewModelFactory(propertyDao)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        val map = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment?
        viewPager = requireActivity().findViewById(R.id.viewpager)
        map?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        setMarkerOnMap()
        googleMap.setOnMarkerClickListener {
            val result = it.tag.toString().toInt()
            setFragmentResult("requestRef", bundleOf("RefBundle" to result))
            if (requireActivity().supportFragmentManager.fragments[0].childFragmentManager.findFragmentById(R.id.fragment_details) != null)
                viewPager.currentItem = 0
            false
        }

        googleMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(
                    LatLng(
                        40.779897,
                        -73.968565
                    ), 10F)
        )
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    googleMap.isMyLocationEnabled = true
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    googleMap.isMyLocationEnabled = true
                }
            }
        }
    }

    private fun setMarkerOnMap() {
        setAllMarkerOnMap()
        setFragmentResultListener("requestSqlToMap") { _, bundle ->
            val result = bundle.getString("SqlMapBundle")
            if (result != null) {
                if (result.isNotEmpty()) {
                    setMarkerWithFilterOnMap(result)
                } else {
                    googleMap.clear()
                    setAllMarkerOnMap()
                }
            }
        }
    }

    private fun setAllMarkerOnMap() {
        var marker: Marker?
        viewModel
            .getAllProperties()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { properties ->
                for (property in properties) {
                    marker = googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(property.latitude, property.longitude))
                            .title(property.address)
                            .snippet(property.type.name)
                    )
                    marker?.tag = property.ref
                }
            }
    }

    private fun setMarkerWithFilterOnMap(result: String) {
        var marker: Marker?
        viewModel.getPropertiesWithFilter(SimpleSQLiteQuery(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { properties ->
                if(properties.isNotEmpty()) {
                    googleMap.clear()
                    for (property in properties) {
                        marker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(property.latitude, property.longitude))
                                .title(property.address)
                                .snippet(property.type.name)
                        )
                        marker?.tag = property.ref
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.isInternetAvailable(requireActivity())) {
            Toast.makeText(
                requireActivity(),
                "Wifi is unavailable to detect your position",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}