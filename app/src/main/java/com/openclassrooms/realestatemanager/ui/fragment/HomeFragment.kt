package com.openclassrooms.realestatemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.HomeFragmentBinding
import com.openclassrooms.realestatemanager.ui.adapter.PropertyRvAdapter
import com.openclassrooms.realestatemanager.viewmodel.HomeViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var rv : RecyclerView

    private lateinit var binding: HomeFragmentBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view_activity_main)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        rv = binding.recyclerViewListPropertiesHomeFragment
        rv.layoutManager = GridLayoutManager(context, 2)

        configPropertiesRv()

        applySort()
    }

    private fun configPropertiesRv(){
        viewModel
            .getAllProperties()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                rv.adapter = PropertyRvAdapter(it)
            }
    }

    private fun applySort() {
            val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_main_activity)
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.sort_price_asc -> {
                        viewModel
                            .getPropertiesWithAscPriceSort()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                rv.adapter = PropertyRvAdapter(it)
                            }
                    }
                    R.id.sort_price_desc -> {
                        viewModel
                            .getPropertiesWithDescPriceSort()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                rv.adapter = PropertyRvAdapter(it)
                            }
                    }
                    R.id.sort_type -> {
                        viewModel
                            .getPropertiesWithTypeSort()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                rv.adapter = PropertyRvAdapter(it)
                            }
                    }
                    R.id.sort_status -> {
                        viewModel
                            .getPropertiesWithStatusSort()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                rv.adapter = PropertyRvAdapter(it)
                            }
                    }
                }
                false
            }
    }

    override fun onResume() {
        applySort()
        super.onResume()
    }
}