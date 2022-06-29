package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
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
import com.openclassrooms.realestatemanager.event.LaunchActivityEvent
import com.openclassrooms.realestatemanager.ui.activity.PropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.PropertyRvAdapter
import com.openclassrooms.realestatemanager.viewmodel.HomeViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


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

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view_activity_main)
        rv = binding.recyclerViewListPropertiesHomeFragment
        return binding.root
    }

    private fun configDetailsFragment(){
        with(binding) {
            if (fragmentDetails != null) {
                fragmentDetails.setTag(fragmentDetails.id, "DetailsFragment")
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_details, PropertyFragment())
                    .commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configDetailsFragment()

        if(binding.fragmentDetails != null){
            rv.layoutManager = GridLayoutManager(context, 1)
        }
        else {
            rv.layoutManager = GridLayoutManager(context, 2)
        }
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

    @Subscribe
    fun onEvent(event: LaunchActivityEvent) {
        if (parentFragmentManager.findFragmentById(R.id.fragment_details) == null) {
                val intent = Intent(activity, PropertyActivity::class.java)
                intent.putExtra("REF", event.ref)
                startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        applySort()
        super.onResume()
    }
}