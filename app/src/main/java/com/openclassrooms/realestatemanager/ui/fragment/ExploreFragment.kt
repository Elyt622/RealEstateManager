package com.openclassrooms.realestatemanager.ui.fragment

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.SupportMapFragment
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ExploreFragmentBinding
import com.openclassrooms.realestatemanager.event.LaunchActivityEvent
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.ui.adapter.PropertyRvAdapter
import com.openclassrooms.realestatemanager.ui.adapter.StatusRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.viewmodel.ExploreViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*


class ExploreFragment : Fragment() {

    private lateinit var binding: ExploreFragmentBinding

    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var viewModel: ExploreViewModel

    private lateinit var datePickerDialog : DatePickerDialog.OnDateSetListener

    private val myCalendar: Calendar = Calendar.getInstance()

    private lateinit var editText: EditText

    private lateinit var rv : RecyclerView

    private var propertiesWithSort: List<Property>? = null

    private lateinit var toolbar: Toolbar

    private lateinit var menuItem: MenuItem

    private lateinit var viewPager: ViewPager2

    private lateinit var map : SupportMapFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExploreFragmentBinding.inflate(layoutInflater)
        toolbar = requireActivity().findViewById(R.id.toolbar)
        viewPager = requireActivity().findViewById(R.id.viewpager)
        menuItem = toolbar.menu.findItem(R.id.home_top_sort).setVisible(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ExploreViewModel::class.java]

        configTypeRv()
        configOptionRv()
        configStatusRv()

        rv = parentFragmentManager
            .fragments[0]
            .requireView()
            .findViewById(R.id.recycler_view_list_properties)

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar, editText)
        }

        with(binding){
            buttonSearch.setOnClickListener {
                        viewModel.applyAllFilters(
                            viewModel.getTypes(),
                            editTextStartPrice.text.toString().toIntOrNull(),
                            editTextEndPrice.text.toString().toIntOrNull(),
                            editTextStartSurface.text.toString().toFloatOrNull(),
                            editTextEndSurface.text.toString().toFloatOrNull(),
                            editTextStartBeds.text.toString().toIntOrNull(),
                            editTextEndBeds.text.toString().toIntOrNull(),
                            editTextStartBathrooms.text.toString().toIntOrNull(),
                            editTextEndBathrooms.text.toString().toIntOrNull(),
                            viewModel.getOptions(),
                            viewModel.getStatus(),
                            viewModel.getStartEntryDate(),
                            viewModel.getEndEntryDate(),
                            viewModel.getStartSoldDate(),
                            viewModel.getEndSoldDate()
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onSuccess = { propertiesWithFilter ->
                                    rv.adapter = PropertyRvAdapter(
                                        applySortAndFilters(
                                            propertiesWithSort,
                                            propertiesWithFilter
                                        )
                                    )
                                    showFragmentDetails(propertiesWithFilter)
                                    map = parentFragmentManager.fragments[3].childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                                    viewModel.setMarkerOnMap(map, propertiesWithFilter)
                                    if(resources.configuration.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE) && propertiesWithFilter.isNotEmpty())
                                        EventBus.getDefault().post(LaunchActivityEvent(propertiesWithFilter[0].ref))
                                    viewPager.currentItem = 0
                                },
                                onError = {
                                    Toast.makeText(
                                        activity,
                                        "No filter selected",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                    }
            buttonReset.setOnClickListener {
                resetAllFields()
                viewModel
                    .getAllProperties()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        allProperties ->
                        rv.adapter = PropertyRvAdapter(allProperties)
                        viewModel.setMarkerOnMap(map, allProperties)
                        showFragmentDetails(allProperties)
                        if(resources.configuration.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE) && allProperties.isNotEmpty())
                            EventBus.getDefault().post(LaunchActivityEvent(allProperties[0].ref))
                        viewPager.currentItem = 0
                    }
            }

            editTextStartEntryDate.setOnClickListener {
                editText = editTextStartEntryDate
                showDatePickerDialog()
            }

            editTextEndEntryDate.setOnClickListener {
                editText = editTextEndEntryDate
                showDatePickerDialog()
            }

            editTextStartSoldDate.setOnClickListener {
                editText = editTextStartSoldDate
                showDatePickerDialog()
            }

            editTextEndSoldDate.setOnClickListener {
                editText = editTextEndSoldDate
                showDatePickerDialog()
            }
        }
    }

    private fun showFragmentDetails(properties: List<Property>) {
        val fragment = requireActivity()
            .supportFragmentManager
            .fragments[0]
            .childFragmentManager
            .findFragmentById(R.id.fragment_details)
        if (fragment != null) {
            if (properties.isEmpty()) {
                requireActivity()
                    .supportFragmentManager
                    .fragments[0]
                    .childFragmentManager
                    .beginTransaction()
                    .hide(
                        fragment
                    ).commit()
                Toast.makeText(requireActivity(), "No results with this filter", Toast.LENGTH_LONG).show()
            }
            else
                requireActivity()
                    .supportFragmentManager
                    .fragments[0]
                    .childFragmentManager
                    .beginTransaction()
                    .show(
                        fragment
                    ).commit()
        }
    }

    private fun applySortAndFilters(propertiesWithSort: List<Property>?, propertiesWithFilters: List<Property>?) : List<Property>{
        val propertiesWithSortAndFilters: MutableList<Property> = mutableListOf()
        if (propertiesWithSort != null) {
            for (property in propertiesWithSort) {
                if (propertiesWithFilters != null && propertiesWithFilters.contains(property)) {
                    propertiesWithSortAndFilters.add(property)
                }
                else if (propertiesWithFilters == null){
                    return propertiesWithSort
                }
            }
        }
        else {
            if (propertiesWithFilters != null) {
                return propertiesWithFilters
            }
        }
        return propertiesWithSortAndFilters
    }

    private fun applySort() {
        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.sort_price_asc -> {
                    viewModel
                        .getPropertiesWithAscPriceSort()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            propertiesWithSort = it
                        }
                }
                R.id.sort_price_desc -> {
                    viewModel
                        .getPropertiesWithDescPriceSort()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                            propertiesWithSort = it
                        }
                }
                R.id.sort_type -> {
                    viewModel
                        .getPropertiesWithTypeSort()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                            propertiesWithSort = it
                        }
                }
                R.id.sort_status -> {
                    viewModel
                        .getPropertiesWithStatusSort()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                            propertiesWithSort = it
                        }
                }
            }
            false
        }
    }

    private fun resetAllFields(){
        with(binding) {
            editTextStartPrice.setText("")
            editTextEndPrice.setText("")
            editTextStartSurface.setText("")
            editTextEndSurface.setText("")
            editTextStartBeds.setText("")
            editTextEndBeds.setText("")
            editTextStartBathrooms.setText("")
            editTextEndBathrooms.setText("")
            editTextStartEntryDate.setText("")
            editTextEndEntryDate.setText("")
            editTextStartSoldDate.setText("")
            editTextEndSoldDate.setText("")
        }
        with(viewModel) {
            setTypes(mutableListOf())
            setOptions(mutableListOf())
            setStatus(mutableListOf())
            setStartEntryDate(null)
            setEndEntryDate(null)
            setStartSoldDate(null)
            setEndSoldDate(null)
        }
        configTypeRv()
        configOptionRv()
        configStatusRv()
    }

    private fun showDatePickerDialog(){
        DatePickerDialog(
            requireContext(),
            datePickerDialog,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateLabel(myCalendar: Calendar, editText: EditText){
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        with(binding){
            when(editText){
                editTextStartEntryDate -> {
                    editTextStartEntryDate.setText(sdf.format(myCalendar.time))
                    viewModel.setStartEntryDate(myCalendar.time)
                }
                editTextEndEntryDate -> {
                    editTextEndEntryDate.setText(sdf.format(myCalendar.time))
                    viewModel.setEndEntryDate(myCalendar.time)
                }
                editTextStartSoldDate -> {
                    editTextStartSoldDate.setText(sdf.format(myCalendar.time))
                    viewModel.setStartSoldDate(myCalendar.time)
                }
                editTextEndSoldDate -> {
                    editTextEndSoldDate.setText(sdf.format(myCalendar.time))
                    viewModel.setEndSoldDate(myCalendar.time)
                }
            }
        }
    }

    private fun configTypeRv(){
        with(binding){
            recyclerViewType.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewType.adapter = TypeRvAdapterExploreFragment(
                viewModel,
                Type.values()
            )
        }
    }

    private fun configOptionRv(){
        with(binding){
            recyclerViewOptions.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewOptions.adapter = OptionRvAdapterExploreFragment(
                viewModel,
                Option.values()
            )
        }
    }

    private fun configStatusRv(){
        with(binding){
            recyclerViewStatus.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewStatus.adapter = StatusRvAdapterExploreFragment(
                viewModel,
                Status.values()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        applySort()
    }
}