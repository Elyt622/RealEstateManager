package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.activity.PropertyActivity

class PhotoRvAdapter(private val dataSet: List<String>, private val propertyActivity: PropertyActivity) :
    RecyclerView.Adapter<PhotoRvAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photos_property_recycler_view, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(dataSet[position]).into(holder.image)

        holder.image.setOnClickListener {
            Glide.with(context).load(dataSet[position]).into(propertyActivity.image)
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image_view_photo_element_rv)
    }
}