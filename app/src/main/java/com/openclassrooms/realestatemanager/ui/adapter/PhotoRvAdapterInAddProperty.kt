package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.PhotosAddPropertyRecyclerViewBinding
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel

class PhotoRvAdapterInAddProperty(
    val viewModel: AddPropertyViewModel,
    val context: Context,
    var dataSet: MutableList<Uri>,
    var photosRv: RecyclerView
) : RecyclerView.Adapter<PhotoRvAdapterInAddProperty.ViewHolder>() {

    private lateinit var binding : PhotosAddPropertyRecyclerViewBinding

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            binding = PhotosAddPropertyRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding.root)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(dataSet.isNotEmpty())
                Glide.with(context).load(dataSet[position]).into(holder.image)

            holder.buttonRemove.setOnClickListener{
                dataSet.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, dataSet.size)
                photosRv.isGone = dataSet.isEmpty()
            }
        }

        override fun getItemCount() = dataSet.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val image: ImageView = binding.imageViewPhotoElementRv
            val buttonRemove : Button = binding.buttonRemoveRecyclerViewAddPropertyActivity
        }
}

