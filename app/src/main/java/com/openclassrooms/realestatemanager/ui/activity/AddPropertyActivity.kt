package com.openclassrooms.realestatemanager.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterAddProperty
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapterInAddProperty
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapterAddProperty
import com.openclassrooms.realestatemanager.utils.URIPathHelper
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.*


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

    private lateinit var editTextAgentName: EditText

    private lateinit var buttonAddPhoto : Button

    private lateinit var buttonTakePhoto: Button

    private lateinit var viewModel: AddPropertyViewModel

    private lateinit var editTextDescriptionPhoto : EditText

    private lateinit var binding: ActivityAddPropertyBinding

    private var place: Place? = null

    private lateinit var dir: File

    private lateinit var placesClient: PlacesClient

    private var mutableListOfPhoto : MutableList<Uri> =  mutableListOf()

    private var mutableListDescriptionPhoto : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        dir = getOutputDirectory()

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
        editTextAgentName = binding.editTextAgentName
        editTextDescriptionPhoto = EditText(this)

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
            resultLauncher.launch(Intent(this, CameraActivity::class.java))
        }

        buttonAddProperty.setOnClickListener{
            viewModel.insertProperty(
                Utils.convertStringToFloat(editTextSurface.text.toString()),
                Utils.convertStringToInt(editTextPrice.text.toString()),
                Utils.convertStringToInt(editTextRoom.text.toString()),
                Utils.convertStringToInt(editTextBed.text.toString()),
                Utils.convertStringToInt(editTextBathroom.text.toString()),
                editTextDescription.text.toString(),
                editTextAddress.text.toString(),
                mutableListOfPhoto,
                mutableListDescriptionPhoto,
                place?.latLng?.latitude,
                place?.latLng?.longitude,
                Date(),
                viewModel.getOptions(),
                editTextAgentName.text.toString()
            ).subscribeBy (
                onComplete = {
                    finish()
                },
                onError = {
                    when(it.message){
                        "PHOTO_IS_EMPTY" -> {
                            Toast.makeText(this, "No photo added", Toast.LENGTH_SHORT).show()
                        }
                        "NO_SELECTED_TYPE" -> {
                            Toast.makeText(this, "No selected type", Toast.LENGTH_SHORT).show()
                        }
                        "ROOM_IS_EMPTY" -> {
                            Toast.makeText(this, "Rooms is empty", Toast.LENGTH_SHORT).show()
                        }
                        "BED_IS_EMPTY" -> {
                            Toast.makeText(this, "Beds is empty", Toast.LENGTH_SHORT).show()
                        }
                        "BATHROOM_IS_EMPTY" -> {
                            Toast.makeText(this, "Bathrooms is empty", Toast.LENGTH_SHORT).show()
                        }
                        "PRICE_IS_EMPTY" -> {
                            Toast.makeText(this, "Price is empty", Toast.LENGTH_SHORT).show()
                        }
                        "ADDRESS_IS_EMPTY" -> {
                            Toast.makeText(this, "Address is empty", Toast.LENGTH_SHORT).show()
                        }
                        "DESC_IS_EMPTY" -> {
                            Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show()
                        }
                        "AGENT_IS_EMPTY" -> {
                            Toast.makeText(this, "Agent name is empty", Toast.LENGTH_SHORT).show()
                        }
                        "LOCATION_IS_INVALID" -> {
                            Toast.makeText(this, "Location is invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            ).addTo(bag)
        }
    }

    private fun fillInAddress() {
        val components = place?.addressComponents
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
                    fillInAddress()
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.i("TAG", "User canceled autocomplete")
            }
        } as ActivityResultCallback<ActivityResult>)
    )

    private fun createDialog(data: Intent, fromGallery: Boolean) : AlertDialog {
        val view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_photo_description, null)
        val editTextInput: EditText = view.findViewById(R.id.editText_photo_description)
        return AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                if (editTextInput.text.toString().isNotEmpty()) {
                    if (fromGallery) {
                        mutableListOfPhoto.add(Uri.parse(data.dataString))
                        mutableListDescriptionPhoto.add(editTextInput.text.toString())
                        copyFile(
                            File(
                                URIPathHelper().getPath(
                                    this,
                                    data.data!!
                                ).toString()
                            ),
                            File(
                                "$dir/${
                                    File(
                                        data.dataString.toString()
                                    ).name
                                }.jpg"
                            )
                        )
                    } else {
                        mutableListOfPhoto.add(
                            Uri.parse(
                                data.getStringExtra("URI")
                            )
                        )
                        mutableListDescriptionPhoto.add(
                            editTextInput.text.toString()
                        )
                    }
                    configPhotosRecyclerView()
                } else {
                    Toast.makeText(
                        this,
                        "The description is empty!\nNo photo added!",
                        Toast.LENGTH_LONG)
                        .show()
                }
            }
            .setNegativeButton("CANCEL", null)
            .create()
    }

    private var resultLauncher = registerForActivityResult(
        StartActivityForResult()) {
            result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val data: Intent? = result.data
                if (data != null) {
                    createDialog(data, true).show()
                }
            }
            123 -> {
                val data: Intent? = result.data
                if (data != null) {
                    createDialog(data, false).show()
                }
            }
        }
    }

    private fun copyFile(sourceFile: File, destFile: File) {
        if (!sourceFile.exists()) {
            return
        }
        val source: FileChannel? = FileInputStream(sourceFile).channel
        val destination: FileChannel? = FileOutputStream(destFile).channel
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size())
        }
        source?.close()
        destination?.close()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    private fun configPhotosRecyclerView() {
        photosRv.isGone = mutableListOfPhoto.isEmpty()

        photosRv.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        photosRv.adapter = PhotoRvAdapterInAddProperty(
            viewModel,
            this,
            mutableListOfPhoto,
            mutableListDescriptionPhoto,
            photosRv
        )
    }

    private fun configTypeRecyclerView() {
        typeRv.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        typeRv.adapter = TypeRvAdapterAddProperty(viewModel,
            this,
            Type.values()
        )
    }

    private fun configOptionRecyclerView() {
        optionRv.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        optionRv.adapter = OptionRvAdapterAddProperty(
            viewModel,
            this,
            Option.values()
        )
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