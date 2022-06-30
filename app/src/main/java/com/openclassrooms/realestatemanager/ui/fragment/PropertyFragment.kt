package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var binding: FragmentPropertyBinding

    private lateinit var map: MapView

    private lateinit var modifyPropertyButton: Button

    var ref = 1

    private lateinit var property : Observable<Property>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPropertyBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PropertyViewModel::class.java]
        modifyPropertyButton = binding.buttonModifyPropertyPropertyFragment

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        property = viewModel.getPropertyWithRef(ref)

        loadProperty(property)

        modifyPropertyButton.setOnClickListener{
            val intent = Intent(requireActivity(), ModifyPropertyActivity::class.java)
            intent.putExtra("REF", ref)
            startActivity(intent)
        }
    }

    private fun loadProperty(property: Observable<Property>) {
        property
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (
                onNext = { results ->
                    configPhotosRecyclerView(results.photos)
                    configOptionsRecyclerView(results.options)

                    if(results.photos.isNotEmpty())
                        Glide.with(this).load(results.photos[0]).into(image)

                    configPriceTextView(results.price)
                    configSurfaceTextView(results.surface)
                    configAgentTextView(results.agentName)

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
                        it.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(results.latitude, results.longitude),
                                15F
                            ))
                    }
                },
                onError = {

                }
            )
    }

    @Subscribe
    fun onEvent(event: LaunchActivityEvent) {
        ref = event.ref
            if (parentFragmentManager.findFragmentByTag("DetailsProperty") != null) {
                loadProperty(viewModel.getPropertyWithRef(ref))
            }
    }

    private fun configPhotosRecyclerView(photos: MutableList<Uri>){
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            rvPhoto.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rvPhoto.adapter = PhotoRvAdapter(image, photos)
        } else {
            rvPhoto.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvPhoto.adapter = PhotoRvAdapter(image, photos)
        }
    }

    private fun configOptionsRecyclerView(options: List<Option>?){
        rvOptions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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