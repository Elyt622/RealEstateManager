package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.InterestPoint
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.ui.adapter.InterestPointRvAdapter
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapter
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel

class PropertyActivity : AppCompatActivity() {

    private lateinit var viewModel: PropertyViewModel

    lateinit var image: ImageView

    private lateinit var rvPhoto: RecyclerView

    private lateinit var rvInterestPoint: RecyclerView

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

    private lateinit var toolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property)
        viewModel = ViewModelProvider(this)[PropertyViewModel::class.java]

        image = findViewById(R.id.image_view_main_picture_activity_property)

        // TextView
        typeTextView = findViewById(R.id.textview_type_property_activity)
        priceTextView = findViewById(R.id.textview_price_property_activity)
        surfaceTextView = findViewById(R.id.textview_surface_property_activity)
        descriptionTextView = findViewById(R.id.textview_description_property_activity)
        stateTextView = findViewById(R.id.textview_state_property_activity)
        bedTextView = findViewById(R.id.textview_beds_property_activity)
        roomTextView = findViewById(R.id.textview_rooms_property_activity)
        bathroomTextView = findViewById(R.id.textview_bathroom_property_activity)
        addressTextView = findViewById(R.id.textview_address_property_activity)
        referenceTextView = findViewById(R.id.textview_reference_property_activity)
        entryDateTextView = findViewById(R.id.textview_entry_date_property_activity)
        agentTextView = findViewById(R.id.textview_agent_property_activity)
        titleText = findViewById(R.id.textview_type_top_toolbar_property_activity)
        toolbar = findViewById(R.id.top_toolbar_property_activity)

        // RecyclerView
        rvPhoto = findViewById(R.id.recycler_view_photos_property_activity)
        rvInterestPoint = findViewById(R.id.recycler_view_interest_point_property_activity)

        val id = intent.getIntExtra("ID", -1)
        val property : Property = viewModel.getAllProperties()[id]

        configPhotosRecyclerView(property.photos)
        configInterestPointRecyclerView(property.interestPoint)

        Glide.with(this).load(property.photos[0]).into(image)

        configPriceTextView(property.price)
        configSurfaceTextView(property.surface)
        configAgentTextView(property.agentName)
        configToolbar()

        entryDateTextView.text = """ ${Utils.todayDate}"""
        referenceTextView.text = property.ref.toString()
        addressTextView.text = property.address
        bathroomTextView.text = property.numberBathroom.toString()
        roomTextView.text = property.numberPiece.toString()
        bedTextView.text = property.numberBeds.toString()
        typeTextView.text = property.type.name
        stateTextView.text = property.state.displayName
        descriptionTextView.text = property.description
        titleText.text = property.type.name
    }

    private fun configPhotosRecyclerView(photos: List<String>){
        rvPhoto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPhoto.adapter = PhotoRvAdapter(photos, this)
    }

    private fun configInterestPointRecyclerView(interestPoint: List<InterestPoint>?){
        rvInterestPoint.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (interestPoint != null){
            rvInterestPoint.adapter = InterestPointRvAdapter(interestPoint)
        }else {
            rvInterestPoint.isGone = true
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
        val price = "$priceInt $"
        priceTextView.text = (price)
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
}