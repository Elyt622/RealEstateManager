package com.openclassrooms.realestatemanager.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.CardviewRecyclerViewBinding
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.activity.ModifyPropertyActivity
import com.openclassrooms.realestatemanager.viewmodel.ModifyPropertyViewModel

class TypeRvAdapterModifyProperty(
    val viewModel: ModifyPropertyViewModel,
    private val context: ModifyPropertyActivity,
    private val dataSet: Array<Type>,
)
    : RecyclerView.Adapter<TypeRvAdapterModifyProperty.ViewHolder>() {

    private var clickedItem = viewModel.getType().ordinal

        private lateinit var binding: CardviewRecyclerViewBinding

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            binding = CardviewRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding.root)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.textView.text = dataSet[position].name

            if (clickedItem != -1) {
                if (clickedItem == position) {
                    holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
                    holder.textView.setTextColor(Color.WHITE)
                } else {
                    holder.cardView.setCardBackgroundColor(Color.WHITE)
                    holder.textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                }
            }

            holder.cardView.setOnClickListener {
                viewModel.setType(dataSet[position])
                if (clickedItem != -1) {
                    notifyItemChanged(clickedItem)
                }
                clickedItem = holder.adapterPosition
                notifyItemChanged(clickedItem)
            }
        }

        override fun getItemCount() = dataSet.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = binding.textviewInterestPointRvPropertyActivity
            val cardView: CardView = binding.cardviewOptionsRvOptions
        }
}

