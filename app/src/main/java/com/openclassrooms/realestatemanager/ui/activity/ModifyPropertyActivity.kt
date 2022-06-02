package com.openclassrooms.realestatemanager.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
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
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterModifyPropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapterInModifyProperty
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapter2
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class ModifyPropertyActivity : AppCompatActivity() {

    private lateinit var mutableListOfPhoto: MutableList<Uri>

    private lateinit var binding: ActivityModifyPropertyBinding

    private lateinit var viewModel: ModifyPropertyViewModel

    private lateinit var rvPhoto: RecyclerView

    private lateinit var rvOption: RecyclerView

    private lateinit var rvType: RecyclerView

    private lateinit var priceEditText: EditText

    private lateinit var descriptionEditText: EditText

    private lateinit var surfaceEditText: EditText

    private lateinit var stateEditText: TextView

    private lateinit var bedEditText: EditText

    private lateinit var roomEditText: EditText

    private lateinit var bathroomEditText: EditText

    private lateinit var addressEditText: EditText

    private lateinit var referenceTextView: TextView

    private lateinit var entryDateTextView: TextView

    private lateinit var agentEditText: EditText

    private lateinit var topToolbar : Toolbar

    private lateinit var savePropertyButton: Button

    private lateinit var placesClient: PlacesClient

    private lateinit var place: Place

    private lateinit var type: Type

    private var listOptions: MutableList<Option>? = null

    private lateinit var addPhotoButton: Button

    private lateinit var takePhotoButton: Button

    private lateinit var statusSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ModifyPropertyViewModel::class.java]

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

        val ref = intent.getIntExtra("REF", -1)

        val property = viewModel.getPropertyWithRef(ref)

        property
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy (
                onNext = {
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
                    type = it.type
                    listOptions = it.options
                    statusSpinner.setSelection(it.status.ordinal)

                    configTypeRecyclerView()
                    configOptionRecyclerView()
                    configPhotosRecyclerView()
                    configSpinner()
                },
                onError = {
                    Log.d("DEBUG", it.message.toString())
                },
                onComplete = {

                }
            )
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
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {

                //TODO move photo to a specific repertory
                mutableListOfPhoto.add(Uri.parse(data.data.toString()))
            }
        }
        else if (result.resultCode == 123){
            val uriString = result.data?.getStringExtra("URI")
            uriString?.toUri()?.let { mutableListOfPhoto.add(it) }
        }
        configPhotosRecyclerView()
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
            rvPhoto
        )
    }

    private fun configTypeRecyclerView() {
        rvType.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvType.adapter = TypeRvAdapter2(viewModel,
            this,
            Type.values(),
            type
        )
    }

    private fun configOptionRecyclerView() {
        rvOption.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rvOption.adapter = OptionRvAdapterModifyPropertyActivity(
            viewModel,
            this,
            Option.values(),
            listOptions
        )
    }

    private fun configSpinner(){
        val array: Array<String> = arrayOf(Status.ON_SALE.displayName, Status.SOLD.displayName)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, R.layout.spinner_item_custom, array)
        statusSpinner.adapter = adapter
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