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

class OptionRvAdapterDetails(
    private val dataSet: List<Option>?
    ) : RecyclerView.Adapter<OptionRvAdapterDetails.ViewHolder>() {

    private lateinit var binding : CardviewRecyclerViewBinding

    private lateinit var context: Context

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

            holder.textView.text = dataSet?.get(position)?.displayName
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorAccent
                )
            )
            holder.textView.setTextColor(Color.WHITE)

    }

    override fun getItemCount() = dataSet?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = binding.textviewInterestPointRvPropertyActivity
        val cardView: CardView = binding.cardviewOptionsRvOptions
    }
}
