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
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel

class PhotoRvAdapterInModifyProperty(
    val viewModel: ModifyPropertyViewModel,
    private val mutableListOfPhoto: MutableList<Uri>,
    private val mutableListDescriptionPhoto: MutableList<String>,
    private val rvPhoto: RecyclerView
) : RecyclerView.Adapter<PhotoRvAdapterInModifyProperty.ViewHolder>() {

    private lateinit var binding : PhotosAddPropertyRecyclerViewBinding

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PhotosAddPropertyRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(mutableListOfPhoto.isNotEmpty())
            Glide.with(context).load(mutableListOfPhoto[position]).into(holder.image)
        if (mutableListDescriptionPhoto.isNotEmpty() && mutableListDescriptionPhoto.size == mutableListOfPhoto.size)
            holder.textDescriptionPhoto.text = mutableListDescriptionPhoto[position]

        holder.textDescriptionPhoto.setOnClickListener {
            createDialog(mutableListDescriptionPhoto[position], position).show()
        }

        holder.buttonRemove.setOnClickListener{
            mutableListOfPhoto.removeAt(position)
            mutableListDescriptionPhoto.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, mutableListOfPhoto.size)
            rvPhoto.isGone = mutableListOfPhoto.isEmpty()
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

    override fun getItemCount() = mutableListOfPhoto.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = binding.imageViewPhotoElementRv
        val buttonRemove : Button = binding.buttonRemoveRecyclerView
        val textDescriptionPhoto : TextView = binding.textviewDescriptionPhoto
    }
}
