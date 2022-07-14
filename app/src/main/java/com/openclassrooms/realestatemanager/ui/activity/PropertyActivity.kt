package com.openclassrooms.realestatemanager.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityPropertyBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterDetails
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapter
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class PropertyActivity : BaseActivity() {

    private lateinit var viewModel: PropertyViewModel

    private lateinit var binding: ActivityPropertyBinding

    private lateinit var map: MapView

    private var currencyDollar : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PropertyViewModel::class.java]

        // MapView
        map = binding.map
        map.onCreate(savedInstanceState)

        val ref = intent.getIntExtra("REF", -1)

        val property = viewModel.getPropertyWithRef(ref)

        property
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = { results ->
                    configPhotosRecyclerView(results.descriptionPhoto, results.photos)
                    configOptionsRecyclerView(results.options)

                    if(results.photos.isNotEmpty())
                        Glide.with(this).load(results.photos[0]).into(binding.imageViewMain)

                    configPriceTextView(results.price)
                    configSurfaceTextView(results.surface)
                    configAgentTextView(results.agentName)
                    configToolbar()

                    with(binding) {
                        textviewEntryDate.text = Utils.convertDateToString(results.entryDate)
                        textviewReference.text = results.ref.toString()
                        textviewAddress.text = results.address
                        textviewBathroom.text = results.numberBathroom.toString()
                        textviewRooms.text = results.numberRoom.toString()
                        textviewBeds.text = results.numberBed.toString()
                        textviewType.text = results.type.name
                        textviewState.text = results.status.displayName
                        textviewDescription.text = results.description
                        if (results.soldDate == null) {
                            textviewStaticSoldDate.isGone = true
                            textviewSoldDate.isGone = true
                        } else {
                            textviewStaticSoldDate.isGone = false
                            textviewSoldDate.isGone = false
                            textviewSoldDate.text = Utils.convertDateToString(results.soldDate!!)
                        }
                    }

                    map.getMapAsync {
                        it.addMarker(
                            MarkerOptions()
                            .position(LatLng(
                                results.latitude,
                                results.longitude)
                            ).title(results.address)
                        )
                        it.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    results.latitude,
                                    results.longitude
                                ), 15F)
                        )
                    }
                         },
                onError = {

                }
            )

        binding.buttonConvertToEuro.setOnClickListener {
            currencyDollar = convertCurrency(currencyDollar)
        }

        binding.buttonModifyProperty.setOnClickListener{
            val intent = Intent(
                this,
                ModifyPropertyActivity::class.java
            )
            intent.putExtra("REF", ref)
            startActivity(intent)
        }
    }

    private fun configPriceTextView(price: Int) {
        if (currencyDollar)
            binding.textviewPrice.text = price.toString()
        else
            binding.textviewPrice.text = Utils.convertDollarToEuro(price).toString()
    }

    private fun configPhotosRecyclerView(descriptionPhotos : MutableList<String>, photos: MutableList<Uri>){
        with(binding) {
            recyclerViewPhotos.layoutManager =
                LinearLayoutManager(
                    this@PropertyActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            recyclerViewPhotos.adapter = PhotoRvAdapter(
                descriptionPhotos,
                imageViewMain,
                photos
            )
        }
    }

    private fun configOptionsRecyclerView(options: List<Option>?){
        with(binding) {
            recyclerViewOptions.layoutManager = LinearLayoutManager(
                this@PropertyActivity,
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
        if(surface != null){
            val surfaceFormatted = "$surface m²"
            binding.textviewSurface.text = surfaceFormatted
        } else {
            binding.textviewSurface.text = getString(R.string.not_specified)
        }
    }

    private fun configAgentTextView(name: String){
        val agentFormattedText = " $name"
        binding.textviewAgent.text = agentFormattedText
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

    private fun configToolbar() {
        setSupportActionBar(binding.topToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onStart() {
        super.onStart()
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
        map.onStop()
    }
}