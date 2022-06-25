package com.openclassrooms.realestatemanager.ui.adapter

import android.content.Context
import android.content.res.Configuration
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

class PropertyRvAdapter(private val dataSet: List<Property>) :
    RecyclerView.Adapter<PropertyRvAdapter.ViewHolder>() {

    lateinit var context: Context

    lateinit var config: Configuration

    private lateinit var binding: PropertiesRecyclerViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PropertiesRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        config = parent.context.resources.configuration
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(config.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
            EventBus.getDefault().post(LaunchActivityEvent(dataSet[0].ref))

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
        val typeProperty: TextView = binding.textviewTypePropertyHomeFragment
        val price: TextView = binding.textviewPriceRvHomeFragment
        val address: TextView = binding.textviewAddressRvHomeFragment
        val image: ImageView = binding.imageViewMainPictureRvHomeFragment
        val elementRv: ConstraintLayout = binding.constraintLayoutElementRvHomeFragment
        val beds: TextView = binding.textviewBedsRvHomeFragment
        val bathroom: TextView = binding.textviewBathroomRvHomeFragment
        val rooms: TextView = binding.textviewRoomsRvHomeFragment
        val state: TextView = binding.textviewStateRvHomeFragment
    }
}
