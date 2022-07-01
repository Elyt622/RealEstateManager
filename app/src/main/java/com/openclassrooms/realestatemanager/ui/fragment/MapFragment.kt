package com.openclassrooms.realestatemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.viewmodel.MapViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = MapFragment()
    }

    private lateinit var binding: FragmentMapBinding

    private lateinit var viewModel: MapViewModel

    private lateinit var map: SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        map = SupportMapFragment.newInstance()
        parentFragmentManager
            .beginTransaction()
            .replace(binding.map.id, map, "mapFragment")
            .commit()
        map.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        viewModel
            .getAllProperties()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { properties ->
                for (property in properties)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(property.latitude, property.longitude))
                            .title(property.address)
                            .snippet(property.type.name)
                    )
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

}