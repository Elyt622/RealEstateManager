package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.PhotosAddPropertyRecyclerViewBinding
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel

class PhotoRvAdapterInModifyProperty(
    val viewModel: ModifyPropertyViewModel,
    val context: Context,
    private val mutableListOfPhoto: MutableList<Uri>,
    private val descriptionPhoto: MutableList<String>,
    private val rvPhoto: RecyclerView
) : RecyclerView.Adapter<PhotoRvAdapterInModifyProperty.ViewHolder>() {

    private lateinit var binding : PhotosAddPropertyRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoRvAdapterInModifyProperty.ViewHolder {
        binding = PhotosAddPropertyRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PhotoRvAdapterInModifyProperty.ViewHolder, position: Int) {
        if(mutableListOfPhoto.isNotEmpty())
            Glide.with(context).load(mutableListOfPhoto[position]).into(holder.image)
        if (descriptionPhoto.isNotEmpty() && descriptionPhoto.size == mutableListOfPhoto.size)
            holder.textDescriptionPhoto.text = descriptionPhoto[position]

        holder.buttonRemove.setOnClickListener{
            mutableListOfPhoto.removeAt(position)
            descriptionPhoto.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mutableListOfPhoto.size)
            rvPhoto.isGone = mutableListOfPhoto.isEmpty()
        }
    }

    override fun getItemCount() = mutableListOfPhoto.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = binding.imageViewPhotoElementRv
        val buttonRemove : Button = binding.buttonRemoveRecyclerViewAddPropertyActivity
        val textDescriptionPhoto : TextView = binding.textviewDescriptionPhoto
    }
}
