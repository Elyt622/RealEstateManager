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
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.viewmodel.ExploreViewModel

class TypeRvAdapterExploreFragment (
    private var viewModel: ExploreViewModel,
    private val dataSet: Array<Type>
    ) : RecyclerView.Adapter<TypeRvAdapterExploreFragment.ViewHolder>(){

    private var clickedItems = BooleanArray(dataSet.size) {false}

    private lateinit var context: Context

    private lateinit var binding: CardviewRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardviewRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        context = parent.context
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet[position].name

        if (clickedItems[position]) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
            holder.textView.setTextColor(Color.WHITE)
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE)
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorText))
        }


        holder.cardView.setOnClickListener {
            clickedItems[position] = !clickedItems[position]
            notifyItemChanged(position)
            viewModel.types = viewModel.getTypeWithPositionInRV(clickedItems)
        }
    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = binding.textview
        val cardView: CardView = binding.cardview
    }
}