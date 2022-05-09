package com.openclassrooms.realestatemanager.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.HomeFragmentBinding
import com.openclassrooms.realestatemanager.ui.adapter.PropertyRvAdapter
import com.openclassrooms.realestatemanager.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var rv : RecyclerView

    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        rv = binding.recyclerViewListPropertiesHomeFragment
        rv.layoutManager = GridLayoutManager(context, 2)
        rv.adapter = PropertyRvAdapter(viewModel.getAllProperties().blockingFirst())
    }


    override fun onResume() {
        rv.adapter = PropertyRvAdapter(viewModel.getAllProperties().blockingFirst())
        super.onResume()
    }

    override fun onStart() {
        rv.adapter = PropertyRvAdapter(viewModel.getAllProperties().blockingFirst())
        super.onStart()
    }

}