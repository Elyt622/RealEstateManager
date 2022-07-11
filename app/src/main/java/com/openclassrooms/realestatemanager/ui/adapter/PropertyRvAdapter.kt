package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.databinding.PropertiesRecyclerViewBinding
import com.openclassrooms.realestatemanager.event.LaunchActivityEvent
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.utils.Utils
import org.greenrobot.eventbus.EventBus

class PropertyRvAdapter(private val dataSet: List<Property>)
    : RecyclerView.Adapter<PropertyRvAdapter.ViewHolder>() {

    lateinit var context: Context

    private lateinit var binding: PropertiesRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PropertiesRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.typeProperty.text = dataSet[position].type.name

        val priceString = """${dataSet[position].price} $"""
        viewHolder.price.text = priceString

        viewHolder.address.text = Utils.truncateString(dataSet[position].address, 16)
        viewHolder.typeProperty.text = dataSet[position].type.name

        viewHolder.beds.text = dataSet[position].numberBed.toString()
        viewHolder.bathroom.text = dataSet[position].numberBathroom.toString()
        viewHolder.rooms.text = dataSet[position].numberRoom.toString()
        viewHolder.state.text = dataSet[position].status.displayName

        Glide.with(context).load(dataSet[position].photos[0]).into(viewHolder.image)

        viewHolder.elementRv.setOnClickListener {
            EventBus.getDefault().post(LaunchActivityEvent(dataSet[position].ref))
        }
    }

    override fun getItemCount() = dataSet.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeProperty: TextView = binding.textviewTypeProperty
        val price: TextView = binding.textviewPriceRv
        val address: TextView = binding.textviewAddressRv
        val image: ImageView = binding.imageViewMainPictureRv
        val elementRv: ConstraintLayout = binding.constraintLayoutElementRv
        val beds: TextView = binding.textviewBedsRv
        val bathroom: TextView = binding.textviewBathroomRv
        val rooms: TextView = binding.textviewRoomsRv
        val state: TextView = binding.textviewStateRv
    }
}
