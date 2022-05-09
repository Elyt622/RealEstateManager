package com.openclassrooms.realestatemanager.ui.activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityPropertyBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterDetailsActivity
import com.openclassrooms.realestatemanager.ui.adapter.PhotoRvAdapter
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.viewmodel.PropertyViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class PropertyActivity : BaseActivity() {

    private lateinit var viewModel: PropertyViewModel

    private lateinit var image: ImageView

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

    private lateinit var binding: ActivityPropertyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PropertyViewModel::class.java]

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
        titleText = binding.textviewTypeTopToolbarPropertyActivity
        toolbar = binding.topToolbarPropertyActivity

        // RecyclerView
        rvPhoto = binding.recyclerViewPhotosPropertyActivity
        rvInterestPoint = binding.recyclerViewInterestPointPropertyActivity

        val ref = intent.getIntExtra("REF", -1)

        val property = viewModel.getPropertyWithRef(ref)

        property.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribeBy (
            onNext = { results ->
                    configPhotosRecyclerView(results.photos)
                    configInterestPointRecyclerView(results.options)

                    if(results.photos.isNotEmpty())
                        Glide.with(this).load(results.photos[0]).into(image)

                    configPriceTextView(results.price)
                    configSurfaceTextView(results.surface)
                    configAgentTextView(results.agentName)
                    configToolbar()

                    entryDateTextView.text = """ ${Utils.todayDate} """
                    referenceTextView.text = results.ref.toString()
                    addressTextView.text = results.address
                    bathroomTextView.text = results.numberBathroom.toString()
                    roomTextView.text = results.numberRoom.toString()
                    bedTextView.text = results.numberBed.toString()
                    typeTextView.text = results.type.name
                    stateTextView.text = results.status.displayName
                    descriptionTextView.text = results.description
        },
            onError = {

            },
            onComplete = {

            }
        )
    }

    private fun configPhotosRecyclerView(photos: MutableList<Uri>){
        rvPhoto.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvPhoto.adapter = PhotoRvAdapter(image, photos)
    }

    private fun configInterestPointRecyclerView(options: List<Option>?){
        rvInterestPoint.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        if (options != null){
            rvInterestPoint.adapter = OptionRvAdapterDetailsActivity(options)
        } else {
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
        val priceText = "$priceInt $"
        priceTextView.text = priceText
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