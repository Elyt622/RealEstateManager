package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy

class OptionRvAdapterAddPropertyActivity (val viewModel: AddPropertyViewModel, val context: Context, private val dataSet: Array<Option>)
    : RecyclerView.Adapter<OptionRvAdapterAddPropertyActivity.ViewHolder>(){

    private var clickedItems = BooleanArray(dataSet.size) {false}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet[position].displayName

            if (clickedItems[position]) {
                holder.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimaryDark
                    )
                )
            } else {
                holder.cardView.setCardBackgroundColor(Color.WHITE)
            }

        holder.cardView.setOnClickListener {
            clickedItems[position] = !clickedItems[position]
            notifyItemChanged(position)
            viewModel.getNewProperty().subscribeBy (
                onNext = { results ->
                        results.options = viewModel.getOptionsWithPositionInRV(clickedItems)
                },
                onComplete = {

                },
                onError = {

                })
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textview_interest_point_rv_property_activity)
        val cardView: CardView = view.findViewById(R.id.cardview_options_rv_options)
    }
}