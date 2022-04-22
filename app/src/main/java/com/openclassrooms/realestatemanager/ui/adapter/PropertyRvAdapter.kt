package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.ui.activity.PropertyActivity
import com.openclassrooms.realestatemanager.utils.Utils

class PropertyRvAdapter(private val dataSet: List<Property>) :
    RecyclerView.Adapter<PropertyRvAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.properties_recycler_view, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.typeProperty.text = dataSet[position].type.name

        val priceString = """${dataSet[position].price} $"""
        viewHolder.price.text = priceString

        viewHolder.address.text = Utils.truncateString(dataSet[position].address, 16)
        viewHolder.typeProperty.text = dataSet[position].type.name

        viewHolder.beds.text = dataSet[position].numberBeds.toString()
        viewHolder.bathroom.text = dataSet[position].numberBathroom.toString()
        viewHolder.rooms.text = dataSet[position].numberPiece.toString()
        viewHolder.state.text = dataSet[position].status.displayName

        Glide.with(context).load(dataSet[position].photos[0]).into(viewHolder.image)

        viewHolder.elementRv.setOnClickListener {
            val intent = Intent(context, PropertyActivity::class.java)
            intent.putExtra("ID", position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeProperty: TextView = view.findViewById(R.id.textview_type_property_home_fragment)
        val price: TextView = view.findViewById(R.id.textview_price_rv_home_fragment)
        val address: TextView = view.findViewById(R.id.textview_address_rv_home_fragment)
        val image: ImageView = view.findViewById(R.id.image_view_main_picture_rv_home_fragment)
        val elementRv: ConstraintLayout = view.findViewById(R.id.constraint_layout_element_rv_home_fragment)
        val beds: TextView = view.findViewById(R.id.textview_beds_rv_home_fragment)
        val bathroom: TextView = view.findViewById(R.id.textview_bathroom_rv_home_fragment)
        val rooms: TextView = view.findViewById(R.id.textview_rooms_rv_home_fragment)
        val state: TextView = view.findViewById(R.id.textview_state_rv_home_fragment)
    }
}
