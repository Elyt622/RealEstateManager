package com.openclassrooms.realestatemanager.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
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

    private lateinit var editTextAddress: EditText

    private lateinit var editTextDescription: EditText

    private lateinit var buttonAddPhoto : Button

    private lateinit var viewModel: AddPropertyViewModel

    private var mutableListOfPhoto : MutableList<Uri> =  mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_property)

        viewModel = ViewModelProvider(this)[AddPropertyViewModel::class.java]

        topToolbar = findViewById(R.id.toolbar_on_top_add_property_activity)

        typeRv = findViewById(R.id.recyclerview_type_add_property_activity)
        photosRv = findViewById(R.id.recyclerview_photos_add_property_activity)
        optionRv = findViewById(R.id.recyclerview_option_add_property_activity)

        buttonAddProperty = findViewById(R.id.button_add_new_property_add_property_activity)
        buttonAddPhoto = findViewById(R.id.button_add_photo_add_property_activity)

        editTextAddress = findViewById(R.id.editText_address_add_property_activity)
        editTextBathroom = findViewById(R.id.editText_bathrooms_add_property_activity)
        editTextBed = findViewById(R.id.editText_beds_add_property_activity)
        editTextDescription = findViewById(R.id.editText_description_add_property_activity)
        editTextPrice = findViewById(R.id.editText_price_add_property_activity)
        editTextRoom = findViewById(R.id.editText_rooms_add_property_activity)
        editTextSurface = findViewById(R.id.editText_surface_add_property_activity)

        configToolbar()
        configTypeRecyclerView()
        configOptionRecyclerView()
        configPhotosRecyclerView()

        buttonAddPhoto.setOnClickListener{
            openSomeActivityForResult()
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
                mutableListOfPhoto
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

    private fun openSomeActivityForResult() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
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