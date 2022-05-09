package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PhotosPropertyRecyclerViewBinding

class PhotoRvAdapter(
    val mainImage: ImageView,
    private val dataSet: MutableList<Uri>,
) : RecyclerView.Adapter<PhotoRvAdapter.ViewHolder>() {

    lateinit var context: Context

    private lateinit var binding : PhotosPropertyRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PhotosPropertyRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataSet[position].toString()).into(holder.image)

        holder.image.setOnClickListener {
            Glide.with(context).load(dataSet[position].toString()).into(mainImage)
        }
    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = binding.imageViewPhotoElementRv
    }
}