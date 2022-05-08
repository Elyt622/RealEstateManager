package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.viewmodel.AddPropertyViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoRvAdapterInAddProperty(
    val viewModel: AddPropertyViewModel,
    val context: Context,
    var dataSet: MutableList<Uri>,
    var photosRv: RecyclerView
) : RecyclerView.Adapter<PhotoRvAdapterInAddProperty.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photos_add_property_recycler_view, parent, false)
            return ViewHolder(view)
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

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val image: ImageView = view.findViewById(R.id.image_view_photo_element_rv)
            val buttonRemove : Button = view.findViewById(R.id.button_remove_recycler_view_add_property_activity)
        }
}

