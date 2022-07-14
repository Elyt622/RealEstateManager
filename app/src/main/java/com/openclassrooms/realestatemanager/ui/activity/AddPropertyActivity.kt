package com.openclassrooms.realestatemanager.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.databinding.ActivityAddPropertyBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterAddProperty
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapterInAddProperty
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapterAddProperty
import com.openclassrooms.realestatemanager.utils.URIPathHelper
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.*

class AddPropertyActivity : BaseActivity() {

    private lateinit var viewModel: AddPropertyViewModel

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

        configToolbar()
        configTypeRecyclerView()
        configOptionRecyclerView()
        configPhotosRecyclerView()

        with(binding){

            editTextAddress.setOnClickListener {
                startAutocompleteIntent()
            }

            buttonAddPhoto.setOnClickListener{
                ActivityCompat.requestPermissions(
                    this@AddPropertyActivity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    123
                )
                readPermissionGranted()
                writePermissionGranted()
            }

            buttonTakePhoto.setOnClickListener{
                resultLauncher.launch(
                    Intent(
                        this@AddPropertyActivity,
                        CameraActivity::class.java
                    )
                )
            }

            buttonAddNewProperty.setOnClickListener{
                viewModel.insertProperty(
                    editTextSurface.text.toString().toFloatOrNull(),
                    editTextPrice.text.toString().toIntOrNull(),
                    editTextRooms.text.toString().toIntOrNull(),
                    editTextBeds.text.toString().toIntOrNull(),
                    editTextBathrooms.text.toString().toIntOrNull(),
                    editTextDescription.text.toString(),
                    editTextAddress.text.toString(),
                    getAddressArea(place?.latLng?.latitude!!, place?.latLng?.longitude!!),
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
                        sendNotification()
                    },
                    onError = {
                        when(it.message){
                            "PHOTO_IS_EMPTY" -> showToast("No photo added")
                            "NO_SELECTED_TYPE" -> showToast("No selected type")
                            "ROOM_IS_EMPTY" -> showToast("Rooms is empty")
                            "BED_IS_EMPTY" -> showToast("Beds is empty")
                            "BATHROOM_IS_EMPTY" -> showToast("Bathrooms is empty")
                            "PRICE_IS_EMPTY" -> showToast("Price is empty")
                            "ADDRESS_IS_EMPTY" -> showToast("Address is empty")
                            "DESC_IS_EMPTY" -> showToast("Description is empty")
                            "AGENT_IS_EMPTY" -> showToast("Agent name is empty")
                            "LOCATION_IS_INVALID" -> showToast("Location is invalid")
                        }
                    }
                ).addTo(bag)
            }
        }
    }

    private fun sendNotification() {
        val title = "New property added"
        val builder = NotificationCompat.Builder(
            this,
            App.NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.logo_toolbar)
            .setContentTitle(title)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(0, builder.build())
    }

    private fun readPermissionGranted() : Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun writePermissionGranted() : Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (readPermissionGranted() && writePermissionGranted()) {
            openSomeActivityForResult()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        binding.editTextAddress.setText(address1.toString())
    }

    private fun getAddressArea(latitude: Double, longitude: Double) : String{
        val geocoder = Geocoder(this, Locale.US)
        return geocoder.getFromLocation(latitude, longitude, 1)[0].subLocality
    }

    private fun startAutocompleteIntent() {

        val fields: List<Place.Field> = listOf(
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG, Place.Field.VIEWPORT
        )

        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
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
        val view = LayoutInflater
            .from(this)
            .inflate(R.layout.alert_dialog_photo_description, null)
        val editTextInput: EditText =
            view.findViewById(R.id.editText_photo_description)
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
        with(binding) {
            recyclerviewPhotos.isGone = mutableListOfPhoto.isEmpty()
            recyclerviewPhotos.layoutManager = LinearLayoutManager(
                this@AddPropertyActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerviewPhotos.adapter = PhotoRvAdapterInAddProperty(
                viewModel,
                mutableListOfPhoto,
                mutableListDescriptionPhoto,
                recyclerviewPhotos
            )
        }
    }

    private fun configTypeRecyclerView() {
        with(binding) {
            recyclerviewType.layoutManager = LinearLayoutManager(
                this@AddPropertyActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerviewType.adapter = TypeRvAdapterAddProperty(
                viewModel,
                Type.values()
            )
        }
    }

    private fun configOptionRecyclerView() {
        with(binding) {
            recyclerviewOption.layoutManager = LinearLayoutManager(
                this@AddPropertyActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerviewOption.adapter = OptionRvAdapterAddProperty(
                viewModel,
                Option.values()
            )
        }
    }

    private fun configToolbar() {
        setSupportActionBar(binding.toolbarTop)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}