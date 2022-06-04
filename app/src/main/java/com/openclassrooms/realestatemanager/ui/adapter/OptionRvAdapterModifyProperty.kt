package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.CardviewRecyclerViewBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel

class OptionRvAdapterModifyProperty(
    val viewModel: ModifyPropertyViewModel,
    val context: Context,
    private val dataset: Array<Option>,
    ) : RecyclerView.Adapter<OptionRvAdapterModifyProperty.ViewHolder>() {

    private lateinit var binding : CardviewRecyclerViewBinding

    private var clickedItems: BooleanArray = viewModel.getBooleanArrayWithListOptions(viewModel.getOptions())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionRvAdapterModifyProperty.ViewHolder {
        binding = CardviewRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(
        holder: OptionRvAdapterModifyProperty.ViewHolder,
        position: Int
    ) {
        holder.textView.text = dataset[position].displayName

        if (clickedItems[position]) {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorAccent
                )
            )
            holder.textView.setTextColor(Color.WHITE)
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorText))
        }

        holder.cardView.setOnClickListener {
            clickedItems[position] = !clickedItems[position]
            viewModel.setOptions(viewModel.getOptionsWithPositionInRV(clickedItems))
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = dataset.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = binding.textviewInterestPointRvPropertyActivity
        val cardView: CardView = binding.cardviewOptionsRvOptions
    }
}
