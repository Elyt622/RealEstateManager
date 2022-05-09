package com.openclassrooms.realestatemanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.databinding.CardviewRecyclerViewBinding
import com.openclassrooms.realestatemanager.model.Option

class OptionRvAdapterDetailsActivity(private val dataSet: List<Option>?)
    : RecyclerView.Adapter<OptionRvAdapterDetailsActivity.ViewHolder>() {

    private lateinit var binding : CardviewRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardviewRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet?.get(position)?.displayName
    }

    override fun getItemCount() = dataSet?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = binding.textviewInterestPointRvPropertyActivity
    }
}
