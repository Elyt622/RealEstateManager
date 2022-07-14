package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentPropertyBinding
import com.openclassrooms.realestatemanager.event.LaunchActivityEvent
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.ui.activity.ModifyPropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterDetails
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapter
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class PropertyFragment : Fragment() {

    private lateinit var viewModel: PropertyViewModel

    private lateinit var binding: FragmentPropertyBinding

    private lateinit var map: MapView

    var ref = 1

    private lateinit var property : Observable<Property>

    private var currencyDollar : Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PropertyViewModel::class.java]

        // MapView
        map = binding.map
        map.onCreate(savedInstanceState)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        property = viewModel.getPropertyWithRef(ref)

        loadProperty(property)

        with(binding) {
            buttonModifyProperty.setOnClickListener {
                val intent = Intent(requireActivity(), ModifyPropertyActivity::class.java)
                intent.putExtra("REF", ref)
                startActivity(intent)
            }

            buttonConvertToEuro.setOnClickListener {
                currencyDollar = convertCurrency(currencyDollar)
            }
        }
    }

    private fun loadProperty(property: Observable<Property>) {
        property
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = { results ->
                    with(binding){

                        configPhotosRecyclerView(results.descriptionPhoto, results.photos)
                        configOptionsRecyclerView(results.options)

                        if(results.photos.isNotEmpty())
                            Glide.with(this@PropertyFragment).load(results.photos[0]).into(imageViewMain)

                        configSurfaceTextView(results.surface)
                        configAgentTextView(results.agentName)
                        configPriceTextView(results.price)

                        textviewEntryDate.text = Utils.convertDateToString(results.entryDate)
                        textviewReference.text = results.ref.toString()
                        textviewAddress.text = results.address
                        textviewBathroom.text = results.numberBathroom.toString()
                        textviewRooms.text = results.numberRoom.toString()
                        textviewBeds.text = results.numberBed.toString()
                        textviewType.text = results.type.name
                        textviewState.text = results.status.displayName
                        textviewDescription.text = results.description

                        if(results.soldDate == null){
                            textviewStaticSoldDate.isGone = true
                            textviewSoldDate.isGone = true
                        } else {
                            textviewStaticSoldDate.isGone = false
                            textviewSoldDate.isGone = false
                            textviewSoldDate.text = Utils.convertDateToString(results.soldDate!!)
                        }

                    map.getMapAsync {
                        it.addMarker(
                            MarkerOptions()
                                .position(LatLng(results.latitude, results.longitude))
                                .title(results.address))
                        it.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(results.latitude, results.longitude),
                                15F
                            ))
                    }
                    }
                         },
                onError = {

                }
            )
    }

    private fun configPriceTextView(price: Int) {
        if (currencyDollar)
            binding.textviewPrice.text = price.toString()
        else
            binding.textviewPrice.text = Utils.convertDollarToEuro(price).toString()
    }

    @Subscribe
    fun onEvent(event: LaunchActivityEvent) {
        ref = event.ref
            if (parentFragmentManager.findFragmentById(R.id.fragment_details) != null) {
                loadProperty(viewModel.getPropertyWithRef(ref))
            }
    }

    private fun configPhotosRecyclerView(descriptionPhoto: MutableList<String>, photos: MutableList<Uri>){
        with(binding) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recyclerViewPhotos.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                recyclerViewPhotos.adapter = PhotoRvAdapter(
                    descriptionPhoto,
                    imageViewMain,
                    photos
                )
            } else {
                recyclerViewPhotos.layoutManager =
                    LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                recyclerViewPhotos.adapter = PhotoRvAdapter(
                    descriptionPhoto,
                    imageViewMain,
                    photos
                )
            }
        }
    }

    private fun configOptionsRecyclerView(options: List<Option>?){
        with(binding) {
            recyclerViewOptions.layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            if (options != null && options.isNotEmpty()) {
                recyclerViewOptions.adapter = OptionRvAdapterDetails(options)
                recyclerViewOptions.isGone = false
            } else {
                recyclerViewOptions.isGone = true
            }
        }
    }

    private fun configSurfaceTextView(surface: Float?){
        with(binding) {
            if (surface != null) {
                val surfaceFormatted = "$surface m²"
                textviewSurface.text = surfaceFormatted
            } else {
                textviewSurface.text = getString(R.string.not_specified)
            }
        }
    }

    private fun convertCurrency(dollar: Boolean) : Boolean {
        with(binding) {
            if (dollar) {
                textviewPrice.text =
                    Utils.convertDollarToEuro(textviewPrice.text.toString().toInt()).toString()
                textviewCurrencyIcon.text = "€"
                buttonConvertToEuro.text = "$"
                return false
            } else {
                textviewPrice.text =
                    Utils.convertEuroToDollar(textviewPrice.text.toString().toInt()).toString()
                textviewCurrencyIcon.text = "$"
                buttonConvertToEuro.text = "€"
                return true
            }
        }
    }

    private fun configAgentTextView(name: String){
        val agentFormattedText = " $name"
        binding.textviewAgent.text = agentFormattedText
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        map.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
        map.onStop()
    }
}