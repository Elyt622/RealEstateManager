package com.openclassrooms.realestatemanager.ui.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel
import java.util.*

class SpinnerAdapter(val context: Context, val viewModel: ModifyPropertyViewModel) : AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"))
        calendar.time = Date()
        when(p2){
            Status.ON_SALE.ordinal -> {
                viewModel.setSoldDate(null)
            }
            Status.SOLD.ordinal -> {
                if(viewModel.getSoldDate() != null) {
                    // TODO When user change exist date by another
                } else if (viewModel.getSoldDate() == null){
                    DatePickerDialog(
                        context,
                        this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // No need this function
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        Log.d("DEBUG", year.toString() + month + dayOfMonth)
        viewModel.setSoldDate(GregorianCalendar(year, month - 1, dayOfMonth).time)
    }
}