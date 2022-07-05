package com.openclassrooms.realestatemanager.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.PhotosAddPropertyRecyclerViewBinding
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel

class PhotoRvAdapterInAddProperty(
    val viewModel: AddPropertyViewModel,
    val context: Context,
    var dataSet: MutableList<Uri>,
    var mutableListDescriptionPhoto: MutableList<String>,
    var photosRv: RecyclerView
) : RecyclerView.Adapter<PhotoRvAdapterInAddProperty.ViewHolder>() {

    private lateinit var binding: PhotosAddPropertyRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PhotosAddPropertyRecyclerViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataSet.isNotEmpty())
            Glide.with(context).load(dataSet[position]).into(holder.image)
        if (mutableListDescriptionPhoto.isNotEmpty() && mutableListDescriptionPhoto.size == dataSet.size)
            holder.textDescriptionPhoto.text = mutableListDescriptionPhoto[position]

        holder.textDescriptionPhoto.setOnClickListener {
            createDialog(mutableListDescriptionPhoto[position], position).show()
        }

        holder.buttonRemove.setOnClickListener{
            dataSet.removeAt(position)
            mutableListDescriptionPhoto.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataSet.size)
            photosRv.isGone = dataSet.isEmpty()
        }
    }

    private fun createDialog(descriptionPhoto: String, position: Int) : AlertDialog {
        val view =
            LayoutInflater.from(context).inflate(R.layout.alert_dialog_photo_description, null)
        val editTextInput: EditText = view.findViewById(R.id.editText_photo_description)
        editTextInput.setText(descriptionPhoto)
        return AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton("OK") { _, _ ->
                if (editTextInput.text.toString().isNotEmpty()) {
                    mutableListDescriptionPhoto[position] = editTextInput.text.toString()
                    notifyItemChanged(position)
                } else {
                    Toast.makeText(
                        context,
                        "The description is empty!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("CANCEL", null)
            .create()
    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = binding.imageViewPhotoElementRv
        val buttonRemove : Button = binding.buttonRemoveRecyclerViewAddPropertyActivity
        val textDescriptionPhoto : TextView = binding.textviewDescriptionPhoto
    }
}

