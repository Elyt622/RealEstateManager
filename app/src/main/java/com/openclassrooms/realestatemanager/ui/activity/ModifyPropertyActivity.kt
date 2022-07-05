package com.openclassrooms.realestatemanager.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
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

    private lateinit var rvPhoto: RecyclerView

    private lateinit var rvOption: RecyclerView

    private lateinit var rvType: RecyclerView

    private lateinit var priceEditText: EditText

    private lateinit var descriptionEditText: EditText

    private lateinit var surfaceEditText: EditText

    private lateinit var bedEditText: EditText

    private lateinit var roomEditText: EditText

    private lateinit var bathroomEditText: EditText

    private lateinit var addressEditText: EditText

    private lateinit var referenceTextView: TextView

    private lateinit var entryDateTextView: TextView

    private lateinit var soldDateTextView: TextView

    private lateinit var soldDateStaticTextView: TextView

    private lateinit var agentEditText: EditText

    private lateinit var topToolbar : Toolbar

    private lateinit var savePropertyButton: Button

    private lateinit var placesClient: PlacesClient

    private lateinit var place: Place

    private lateinit var addPhotoButton: Button

    private lateinit var takePhotoButton: Button

    private lateinit var statusSpinner: Spinner

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

        viewModel = ViewModelProvider(this)[ModifyPropertyViewModel::class.java]

        val ref = intent.getIntExtra("REF", -1)

        dir = getOutputDirectory()
        property = viewModel.getPropertyWithRef(ref)

        // Initialize the SDK
        Places.initialize(applicationContext, resources.getString(R.string.maps_api_key))
        placesClient = Places.createClient(this)

        //RecyclerView
        rvOption = binding.recyclerViewOptionsModifyPropertyActivity
        rvPhoto = binding.recyclerViewPhotosModifyPropertyActivity
        rvType = binding.recyclerViewTypeModifyPropertyActivity

        //Toolbar
        topToolbar = binding.topToolbarModifyPropertyActivity

        //Button
        savePropertyButton = binding.buttonSavePropertyModifyPropertyActivity
        addPhotoButton = binding.buttonAddPhotoModifyPropertyActivity
        takePhotoButton = binding.buttonTakePhotoModifyPropertyActivity

        //EditText
        priceEditText = binding.edittextPriceModifyPropertyActivity
        descriptionEditText = binding.edittextDescriptionModifyPropertyActivity
        surfaceEditText = binding.edittextSurfaceModifyPropertyActivity
        bedEditText = binding.edittextBedsModifyPropertyActivity
        roomEditText = binding.edittextRoomsModifyPropertyActivity
        bathroomEditText = binding.edittextBathroomModifyPropertyActivity
        addressEditText = binding.edittextAddressModifyPropertyActivity
        agentEditText = binding.edittextAgentModifyPropertyActivity

        //Textview
        referenceTextView = binding.textviewReferenceModifyPropertyActivity
        entryDateTextView = binding.textviewEntryDateModifyPropertyActivity
        soldDateTextView = binding.textviewSoldDateModifyPropertyActivity
        soldDateStaticTextView = binding.textviewStaticSoldDateModifyPropertyActivity

        //Spinner
        statusSpinner = binding.spinnerStatusModifyPropertyActivity

        //Config
        configToolbar()

        //Action Click
        addressEditText.setOnClickListener {
            startAutocompleteIntent()
        }

        addPhotoButton.setOnClickListener {
            openSomeActivityForResult()
        }

        takePhotoButton.setOnClickListener {
            resultLauncher.launch(Intent(this, CameraActivity::class.java))
        }

        savePropertyButton.setOnClickListener {
            viewModel.updateProperty(
                ref,
                viewModel.getType(),
                Utils.convertStringToInt(priceEditText.text.toString()),
                Utils.convertStringToFloat(surfaceEditText.text.toString()),
                Utils.convertStringToInt(roomEditText.text.toString()),
                Utils.convertStringToInt(bedEditText.text.toString()),
                Utils.convertStringToInt(bathroomEditText.text.toString()),
                descriptionEditText.text.toString(),
                mutableListOfPhoto,
                mutableListDescriptionPhoto,
                addressEditText.text.toString(),
                viewModel.getOptions(),
                Status.values()[statusSpinner.selectedItemPosition],
                entryDate,
                viewModel.getSoldDate(),
                agentEditText.text.toString(),
                latitude,
                longitude
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy (
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
            .subscribeBy (
                onNext = {
                    configSpinner()
                    priceEditText.setText(it.price.toString())
                    descriptionEditText.setText(it.description)
                    surfaceEditText.setText(it.surface.toString())
                    bedEditText.setText(it.numberBed.toString())
                    roomEditText.setText(it.numberRoom.toString())
                    bathroomEditText.setText(it.numberBathroom.toString())
                    addressEditText.setText(it.address)
                    referenceTextView.text = it.ref.toString()
                    agentEditText.setText(it.agentName)
                    entryDateTextView.text = Utils.convertDateToString(it.entryDate)
                    mutableListOfPhoto = it.photos
                    mutableListDescriptionPhoto = it.descriptionPhoto
                    viewModel.setType(it.type)
                    viewModel.setOptions(it.options)
                    statusSpinner.setSelection(it.status.ordinal)
                    longitude = it.longitude
                    latitude = it.latitude
                    entryDate = it.entryDate
                    viewModel.setSoldDate(it.soldDate)

                    if(viewModel.getSoldDate() != null){
                        soldDateTextView.isGone = false
                        soldDateStaticTextView.isGone = false
                        soldDateTextView.text = Utils.convertDateToString(viewModel.getSoldDate()!!)
                    } else {
                        soldDateTextView.isGone = true
                        soldDateStaticTextView.isGone = true
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

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorToastMessage(error: String?){
        when(error){
            "PHOTO_IS_EMPTY" -> {
                showToast("No photo added")
            }
            "NO_SELECTED_TYPE" -> {
                showToast("No selected type")
            }
            "ROOM_IS_EMPTY" -> {
                showToast("Rooms is empty")
            }
            "BED_IS_EMPTY" -> {
                showToast("Beds is empty")
            }
            "BATHROOM_IS_EMPTY" -> {
                showToast("Bathrooms is empty")
            }
            "PRICE_IS_EMPTY" -> {
                showToast("Price is empty")
            }
            "ADDRESS_IS_EMPTY" -> {
                showToast("Address is empty")
            }
            "DESC_IS_EMPTY" -> {
                showToast("Description is empty")
            }
            "AGENT_IS_EMPTY" -> {
                showToast("Agent name is empty")
            }
            "LOCATION_IS_INVALID" -> {
                showToast("Location is invalid")
            }
            "SOLD_DATE_IS_EMPTY" -> {
                showToast("Sold date is empty")
                statusSpinner.setSelection(Status.ON_SALE.ordinal)
            }
        }
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
        addressEditText.setText(address1.toString())
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
        rvPhoto.isGone = mutableListOfPhoto.isEmpty()

        rvPhoto.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvPhoto.adapter = PhotoRvAdapterInModifyProperty(
            viewModel,
            this,
            mutableListOfPhoto,
            mutableListDescriptionPhoto,
            rvPhoto
        )
    }

    private fun configTypeRecyclerView() {
        rvType.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvType.adapter = TypeRvAdapterModifyProperty(viewModel,
            this,
            Type.values(),
        )
    }

    private fun configOptionRecyclerView() {
        rvOption.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvOption.adapter = OptionRvAdapterModifyProperty(
            viewModel,
            this,
            Option.values(),
        )
    }

    private fun configSpinner(){
        val array: Array<String> = arrayOf(Status.ON_SALE.displayName, Status.SOLD.displayName)
        adapterSpinner = ArrayAdapter(this, R.layout.spinner_item_custom, array)
        statusSpinner.adapter = adapterSpinner
        statusSpinner.onItemSelectedListener = SpinnerAdapter(this, viewModel)
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