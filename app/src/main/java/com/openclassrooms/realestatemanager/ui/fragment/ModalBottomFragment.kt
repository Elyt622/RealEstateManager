package com.openclassrooms.realestatemanager.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.openclassrooms.realestatemanager.databinding.ExploreFragmentBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.ui.adapter.StatusRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.viewmodel.ExploreViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class ModalBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ExploreFragmentBinding

    private lateinit var viewModel: ExploreViewModel

    private lateinit var datePickerDialog : DatePickerDialog.OnDateSetListener

    private val myCalendar: Calendar = Calendar.getInstance()

    private lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExploreFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ExploreViewModel::class.java]

        configTypeRv()
        configOptionRv()
        configStatusRv()

        datePickerDialog = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCalendar, editText)
        }

        with(binding){
            buttonSearch.setOnClickListener {
                val queryString =
                viewModel.applyAllFilters(
                    autocompleteSearch.text.toString(),
                    viewModel.getTypes(),
                    editTextStartPhotos.text.toString().toIntOrNull(),
                    editTextEndPhotos.text.toString().toIntOrNull(),
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
                setFragmentResult("requestSqlToList", bundleOf("SqlListBundle" to queryString))
                setFragmentResult("requestSqlToMap", bundleOf("SqlMapBundle" to queryString))
                dismiss()
            }

            viewModel.loadAllAddressArea()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy{
                    if(activity != null){
                        val arrayAdapter = ArrayAdapter(
                            requireActivity(),
                            android.R.layout.simple_list_item_1,
                            it
                        )
                        autocompleteSearch.setAdapter(arrayAdapter)
                    }
                }

            autocompleteSearch.setOnClickListener {
                autocompleteSearch.showDropDown()
            }

            autocompleteSearch.setOnFocusChangeListener{ _, _ ->
                autocompleteSearch.showDropDown()
            }

            buttonReset.setOnClickListener {
                resetAllFields()
                setFragmentResult("requestSqlToList", bundleOf("SqlListBundle" to ""))
                setFragmentResult("requestSqlToMap", bundleOf("SqlMapBundle" to ""))
                dismiss()
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


    companion object {
            const val TAG = "ModalBottomSheet"
        }
    }