package com.openclassrooms.realestatemanager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterAddPropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapter
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var topToolbar: Toolbar

    private lateinit var typeRv: RecyclerView

    private lateinit var optionRv: RecyclerView

    private lateinit var buttonAddProperty: Button

    private lateinit var editTextRoom: EditText

    private lateinit var editTextBed: EditText

    private lateinit var editTextBathroom: EditText

    private lateinit var editTextPrice: EditText

    private lateinit var editTextSurface: EditText

    private lateinit var editTextAddress: EditText

    private lateinit var editTextDescription: EditText

    private lateinit var viewModel: AddPropertyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_property)

        viewModel = ViewModelProvider(this)[AddPropertyViewModel::class.java]

        topToolbar = findViewById(R.id.toolbar_on_top_add_property_activity)
        typeRv = findViewById(R.id.recyclerview_type_add_property_activity)
        optionRv = findViewById(R.id.recyclerview_option_add_property_activity)
        buttonAddProperty = findViewById(R.id.button_add_new_property_add_property_activity)

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

        buttonAddProperty.setOnClickListener{
            viewModel.getNewProperty().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread()).subscribeBy (
                onNext = { property ->
                    property.surface = editTextSurface.text.toString().toFloatOrNull()
                    property.price = editTextPrice.text.toString().toInt()
                    property.numberRoom = editTextRoom.text.toString().toInt()
                    property.numberBed = editTextBed.text.toString().toInt()
                    property.numberBathroom = editTextBathroom.text.toString().toInt()
                    property.description = editTextDescription.text.toString()
                    property.address = editTextAddress.text.toString()
                    viewModel.insertPropertyInDatabase(property)
                },
                onError = { Log.d("HOME", it.message.toString()) },
                onComplete = { Log.d("HOME","Completed") }
            )

        }
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