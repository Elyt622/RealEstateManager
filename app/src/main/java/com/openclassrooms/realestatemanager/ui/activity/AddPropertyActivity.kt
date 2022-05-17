package com.openclassrooms.realestatemanager.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterAddPropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapterInAddProperty
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapter
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy


class AddPropertyActivity : BaseActivity() {

    private lateinit var topToolbar: Toolbar

    private lateinit var typeRv: RecyclerView

    private lateinit var optionRv: RecyclerView

    private lateinit var photosRv: RecyclerView

    private lateinit var buttonAddProperty: Button

    private lateinit var editTextRoom: EditText

    private lateinit var editTextBed: EditText

    private lateinit var editTextBathroom: EditText

    private lateinit var editTextPrice: EditText

    private lateinit var editTextSurface: EditText

    private lateinit var editTextDescription: EditText

    private lateinit var editTextAddress: EditText

    private lateinit var buttonAddPhoto : Button

    private lateinit var buttonTakePhoto: Button

    private lateinit var viewModel: AddPropertyViewModel

    private lateinit var binding: ActivityAddPropertyBinding

    private lateinit var placesClient: PlacesClient

    lateinit var place: Place

    private var mutableListOfPhoto : MutableList<Uri> =  mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AddPropertyViewModel::class.java]

        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.maps_api_key))
        placesClient = Places.createClient(this)

        topToolbar = binding.toolbarOnTopAddPropertyActivity

        typeRv = binding.recyclerviewTypeAddPropertyActivity
        photosRv = binding.recyclerviewPhotosAddPropertyActivity
        optionRv = binding.recyclerviewOptionAddPropertyActivity

        buttonAddProperty = binding.buttonAddNewPropertyAddPropertyActivity
        buttonAddPhoto = binding.buttonAddPhotoAddPropertyActivity
        buttonTakePhoto = binding.buttonTakePhotoAddPropertyActivity

        editTextAddress = binding.editTextAddressAddPropertyActivity
        editTextBathroom = binding.editTextBathroomsAddPropertyActivity
        editTextBed = binding.editTextBedsAddPropertyActivity
        editTextDescription = binding.editTextDescriptionAddPropertyActivity
        editTextPrice = binding.editTextPriceAddPropertyActivity
        editTextRoom = binding.editTextRoomsAddPropertyActivity
        editTextSurface = binding.editTextSurfaceAddPropertyActivity

        configToolbar()
        configTypeRecyclerView()
        configOptionRecyclerView()
        configPhotosRecyclerView()

        editTextAddress.setOnClickListener {
            startAutocompleteIntent()
        }

        buttonAddPhoto.setOnClickListener{
            openSomeActivityForResult()
        }

        buttonTakePhoto.setOnClickListener{

        }

        buttonAddProperty.setOnClickListener{
            viewModel.insertProperty(
                editTextSurface.text.toString().toFloat(),
                editTextPrice.text.toString().toInt(),
                editTextRoom.text.toString().toInt(),
                editTextBed.text.toString().toInt(),
                editTextBathroom.text.toString().toInt(),
                editTextDescription.text.toString(),
                editTextAddress.text.toString(),
                mutableListOfPhoto,
                place.latLng!!.latitude,
                place.latLng!!.longitude
            ).subscribeBy (
                onError = {
                    when(it.message){
                        "ADDRESS_IS_EMPTY" -> {
                            Toast.makeText(this, "Address is empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onComplete = {
                    finish()
                }
            ).addTo(bag)
        }
    }

    private fun fillInAddress() {
        val components = place.addressComponents
        val address1 = StringBuilder()

        if (components != null) {
            for (component in components.asList()) {
                when (component.types[0]) {
                    "street_number" -> {
                        address1.insert(0, component.name)
                    }
                    "route" -> {
                        address1.append(" ")
                        address1.append(component.shortName)
                    }
                }
            }
        }
        editTextAddress.setText(address1.toString())
    }

    private fun startAutocompleteIntent() {

        val fields: List<Place.Field> = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG, Place.Field.VIEWPORT
        )

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setCountry("US")
            .setTypeFilter(TypeFilter.ADDRESS)
            .build(this)
        startAutocomplete.launch(intent)
    }

    private fun openSomeActivityForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        resultLauncher.launch(intent)
    }

    private val startAutocomplete = registerForActivityResult(
        StartActivityForResult(), (ActivityResultCallback { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                if (intent != null) {
                    place = Autocomplete.getPlaceFromIntent(intent)
                    Log.d("TAG", "Place: " + place.addressComponents)
                    fillInAddress()
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.i("TAG", "User canceled autocomplete")
            }
        } as ActivityResultCallback<ActivityResult>)
    )

    private var resultLauncher = registerForActivityResult(
        StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                data.data?.let { mutableListOfPhoto.add(it) }
            }
            configPhotosRecyclerView()
        }
    }

    private fun configPhotosRecyclerView() {
        photosRv.isGone = mutableListOfPhoto.isEmpty()
        photosRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photosRv.adapter = PhotoRvAdapterInAddProperty(viewModel, this, mutableListOfPhoto, photosRv)
    }

    private fun configTypeRecyclerView() {
        typeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        typeRv.adapter = TypeRvAdapter(viewModel, this, Type.values())
    }

    private fun configOptionRecyclerView() {
        optionRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        optionRv.adapter = OptionRvAdapterAddPropertyActivity(viewModel, this, Option.values())
    }

    private fun configToolbar() {
        setSupportActionBar(topToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}