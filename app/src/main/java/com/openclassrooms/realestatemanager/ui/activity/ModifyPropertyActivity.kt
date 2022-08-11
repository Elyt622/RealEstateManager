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
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
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
import com.openclassrooms.realestatemanager.databinding.ActivityModifyPropertyBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterModifyProperty
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapterInModifyProperty
import com.openclassrooms.realestatemanager.ui.adapter.SpinnerAdapter
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapterModifyProperty
import com.openclassrooms.realestatemanager.utils.URIPathHelper
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.util.*

class ModifyPropertyActivity : BaseActivity() {

    private lateinit var mutableListOfPhoto: MutableList<Uri>

    private lateinit var binding: ActivityModifyPropertyBinding

    private lateinit var viewModel: ModifyPropertyViewModel

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var placesClient: PlacesClient

    private lateinit var place: Place

    private lateinit var property: Observable<Property>

    private var latitude: Double = 0.0

    private var longitude: Double = 0.0

    private lateinit var entryDate: Date

    private lateinit var adapterSpinner: ArrayAdapter<String>

    private var mutableListDescriptionPhoto: MutableList<String> = mutableListOf()

    private lateinit var dir : File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory(propertyDao)
        viewModel = ViewModelProvider(this, viewModelFactory)[ModifyPropertyViewModel::class.java]

        val ref = intent.getIntExtra("REF", -1)

        dir = getOutputDirectory()
        property = viewModel.getPropertyWithRef(ref)

        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.maps_api_key))
        placesClient = Places.createClient(this)

        //Config
        configToolbar()

        with(binding) {

            //Action Click
            edittextAddress.setOnClickListener {
                startAutocompleteIntent()
            }

            buttonAddPhoto.setOnClickListener {
                ActivityCompat.requestPermissions(
                    this@ModifyPropertyActivity,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    123
                )
                readPermissionGranted()
                writePermissionGranted()
            }

            binding.buttonTakePhoto.setOnClickListener {
                resultLauncher.launch(
                    Intent(
                        this@ModifyPropertyActivity,
                        CameraActivity::class.java
                    )
                )
            }

            binding.buttonSaveProperty.setOnClickListener {

                viewModel.updateProperty(
                    ref,
                    viewModel.type,
                    edittextPrice.text.toString().toIntOrNull(),
                    edittextSurface.text.toString().toFloatOrNull(),
                    edittextRooms.text.toString().toIntOrNull(),
                    edittextBeds.text.toString().toIntOrNull(),
                    edittextBathroom.text.toString().toIntOrNull(),
                    edittextDescription.text.toString(),
                    mutableListOfPhoto,
                    mutableListDescriptionPhoto,
                    edittextAddress.text.toString(),
                    getAddressArea(latitude, longitude),
                    viewModel.options,
                    Status.values()[spinnerStatus.selectedItemPosition],
                    entryDate,
                    viewModel.soldDate,
                    edittextAgent.text.toString(),
                    latitude,
                    longitude
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onComplete = {
                            showToast("Saved")
                            finish()
                        },
                        onError = {
                            showErrorToastMessage(it.message)
                        }
                    )
            }

            property
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onNext = {
                        configSpinner()
                        edittextPrice.setText(it.price.toString())
                        edittextDescription.setText(it.description)
                        edittextSurface.setText(it.surface.toString())
                        edittextBeds.setText(it.numberBed.toString())
                        edittextRooms.setText(it.numberRoom.toString())
                        edittextBathroom.setText(it.numberBathroom.toString())
                        edittextAddress.setText(it.address)
                        textviewReference.text = it.ref.toString()
                        edittextAgent.setText(it.agentName)
                        textviewEntryDate.text = Utils.convertDateToString(it.entryDate)
                        mutableListOfPhoto = it.photos
                        mutableListDescriptionPhoto = it.descriptionPhoto
                        viewModel.type = it.type
                        viewModel.options = it.options
                        spinnerStatus.setSelection(it.status.ordinal)
                        longitude = it.longitude
                        latitude = it.latitude
                        entryDate = it.entryDate
                        viewModel.soldDate = it.soldDate

                        if (viewModel.soldDate != null) {
                            textviewSoldDate.isGone = false
                            textviewStaticSoldDate.isGone = false
                            textviewSoldDate.text =
                                Utils.convertDateToString(viewModel.soldDate!!)
                        } else {
                            textviewSoldDate.isGone = true
                            textviewStaticSoldDate.isGone = true
                        }
                        configTypeRecyclerView()
                        configOptionRecyclerView()
                        configPhotosRecyclerView()
                    },
                    onError = {
                        Log.d("DEBUG", it.message.toString())
                    }
                ).addTo(bag)
        }
    }

    private fun getAddressArea(latitude: Double, longitude: Double) : String{
        val geocoder = Geocoder(this, Locale.US)
        val stringArea = geocoder.getFromLocation(latitude, longitude, 1)[0].subLocality.uppercase()
        return stringArea.ifEmpty { "NOT SPECIFIED" }
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorToastMessage(error: String?){
        when(error){
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
            "SOLD_DATE_IS_EMPTY" -> {
                showToast("Sold date is empty")
                binding.spinnerStatus.setSelection(Status.ON_SALE.ordinal)
            }
        }
    }

    private fun readPermissionGranted() : Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun writePermissionGranted() : Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){
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

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
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

    private fun openSomeActivityForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
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
        binding.edittextAddress.setText(address1.toString())
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

    private val startAutocomplete = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), (ActivityResultCallback { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent: Intent? = result.data
                if (intent != null) {
                    place = Autocomplete.getPlaceFromIntent(intent)
                    latitude = place.latLng?.latitude!!
                    longitude = place.latLng?.longitude!!
                    Log.d("TAG", "Place: " + place.addressComponents)
                    fillInAddress()
                }
            } else if (result.resultCode == RESULT_CANCELED) {
                Log.i("TAG", "User canceled autocomplete")
            }
        } as ActivityResultCallback<ActivityResult>)
    )

    private fun configPhotosRecyclerView() {
        with(binding) {
            recyclerViewPhotos.isGone = mutableListOfPhoto.isEmpty()

            recyclerViewPhotos.layoutManager = LinearLayoutManager(
                this@ModifyPropertyActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewPhotos.adapter = PhotoRvAdapterInModifyProperty(
                viewModel,
                mutableListOfPhoto,
                mutableListDescriptionPhoto,
                recyclerViewPhotos
            )
        }
    }

    private fun configTypeRecyclerView() {
        with(binding) {
            recyclerViewType.layoutManager = LinearLayoutManager(
                this@ModifyPropertyActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewType.adapter = TypeRvAdapterModifyProperty(
                viewModel,
                Type.values(),
            )
        }
    }

    private fun configOptionRecyclerView() {
        with(binding) {
            recyclerViewOptions.layoutManager = LinearLayoutManager(
                this@ModifyPropertyActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewOptions.adapter = OptionRvAdapterModifyProperty(
                viewModel,
                Option.values(),
            )
        }
    }

    private fun configSpinner(){
        val array: Array<String> = arrayOf(Status.ON_SALE.displayName, Status.SOLD.displayName)
        adapterSpinner = ArrayAdapter(this, R.layout.spinner_item_custom, array)
        binding.spinnerStatus.adapter = adapterSpinner
        binding.spinnerStatus.onItemSelectedListener = SpinnerAdapter(this, viewModel)
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
}