package com.openclassrooms.realestatemanager.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var image: ImageView

    private lateinit var rvPhoto: RecyclerView

    private lateinit var rvOptions: RecyclerView

    private lateinit var typeTextView: TextView

    private lateinit var priceTextView: TextView

    private lateinit var descriptionTextView: TextView

    private lateinit var surfaceTextView: TextView

    private lateinit var stateTextView: TextView

    private lateinit var bedTextView: TextView

    private lateinit var roomTextView: TextView

    private lateinit var bathroomTextView: TextView

    private lateinit var addressTextView: TextView

    private lateinit var referenceTextView: TextView

    private lateinit var entryDateTextView: TextView

    private lateinit var agentTextView: TextView

    private lateinit var titleText: TextView

    private lateinit var soldTextView: TextView

    private lateinit var soldStaticTextView: TextView

    private lateinit var toolbar : Toolbar

    private lateinit var binding: ActivityPropertyBinding

    private lateinit var map: MapView

    private lateinit var modifyPropertyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PropertyViewModel::class.java]

        // MapView
        map = binding.map
        map.onCreate(savedInstanceState)

        // ImageView
        image = binding.imageViewMainPictureActivityProperty

        // TextView
        typeTextView = binding.textviewTypePropertyActivity
        priceTextView = binding.textviewPricePropertyActivity
        surfaceTextView = binding.textviewSurfacePropertyActivity
        descriptionTextView = binding.textviewDescriptionPropertyActivity
        stateTextView = binding.textviewStatePropertyActivity
        bedTextView = binding.textviewBedsPropertyActivity
        roomTextView = binding.textviewRoomsPropertyActivity
        bathroomTextView = binding.textviewBathroomPropertyActivity
        addressTextView = binding.textviewAddressPropertyActivity
        referenceTextView = binding.textviewReferencePropertyActivity
        entryDateTextView = binding.textviewEntryDatePropertyActivity
        agentTextView = binding.textviewAgentPropertyActivity
        soldTextView = binding.textviewSoldDatePropertyActivity
        soldStaticTextView = binding.textviewStaticSoldDatePropertyActivity
        titleText = binding.textviewTypeTopToolbarPropertyActivity

        //Toolbar
        toolbar = binding.topToolbarPropertyActivity

        // RecyclerView
        rvPhoto = binding.recyclerViewPhotosPropertyActivity
        rvOptions = binding.recyclerViewOptionsPropertyActivity

        modifyPropertyButton = binding.buttonModifyPropertyPropertyActivity

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
                        Glide.with(this).load(results.photos[0]).into(image)

                    configPriceTextView(results.price)
                    configSurfaceTextView(results.surface)
                    configAgentTextView(results.agentName)
                    configToolbar()

                    entryDateTextView.text = Utils.convertDateToString(results.entryDate)
                    referenceTextView.text = results.ref.toString()
                    addressTextView.text = results.address
                    bathroomTextView.text = results.numberBathroom.toString()
                    roomTextView.text = results.numberRoom.toString()
                    bedTextView.text = results.numberBed.toString()
                    typeTextView.text = results.type.name
                    stateTextView.text = results.status.displayName
                    descriptionTextView.text = results.description
                    if(results.soldDate == null){
                        soldStaticTextView.isGone = true
                        soldTextView.isGone = true
                    } else {
                        soldStaticTextView.isGone = false
                        soldTextView.isGone = false
                        soldTextView.text = Utils.convertDateToString(results.soldDate!!)
                    }

                    map.getMapAsync {
                        it.addMarker(
                            MarkerOptions()
                            .position(LatLng(results.latitude, results.longitude))
                            .title(results.address))
                        it.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(results.latitude, results.longitude),
                            15F
                        ))
                    }
                         },
            onError = {

            }
            )

        modifyPropertyButton.setOnClickListener{
            val intent = Intent(this, ModifyPropertyActivity::class.java)
            intent.putExtra("REF", ref)
            startActivity(intent)
        }
    }

    private fun configPhotosRecyclerView(descriptionPhotos : MutableList<String>, photos: MutableList<Uri>){
        rvPhoto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPhoto.adapter = PhotoRvAdapter(descriptionPhotos, image, photos)
    }

    private fun configOptionsRecyclerView(options: List<Option>?){
        rvOptions.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (options != null && options.isNotEmpty()){
            rvOptions.adapter = OptionRvAdapterDetails(options)
            rvOptions.isGone = false
        } else {
            rvOptions.isGone = true
        }
    }

    private fun configSurfaceTextView(surface: Float?){
        if(surface != null){
            val surfaceFormatted = "$surface m²"
            surfaceTextView.text = surfaceFormatted
        } else {
            surfaceTextView.text = getString(R.string.not_specified)
        }
    }

    private fun configAgentTextView(name: String){
        val agentFormattedText = " $name"
        agentTextView.text = agentFormattedText
    }

    private fun configPriceTextView(priceInt: Int){
        val priceText = "$priceInt $"
        priceTextView.text = priceText
    }

    private fun configToolbar() {
        setSupportActionBar(toolbar)
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