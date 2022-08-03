package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.HomeFragmentBinding
import com.openclassrooms.realestatemanager.event.LaunchActivityEvent
import com.openclassrooms.realestatemanager.ui.activity.PropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.PropertyRvAdapter
import com.openclassrooms.realestatemanager.ui.custom.ModalBottomSheet
import com.openclassrooms.realestatemanager.viewmodel.HomeViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var binding: HomeFragmentBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestRef") { _, bundle ->
            val result = bundle.getInt("RefBundle")
            EventBus.getDefault().post(LaunchActivityEvent(result))
        }
        setFragmentResultListener("requestSql") { _, bundle ->
            val result = bundle.getString("RefBundle")
            if (result != null) {
                getResultToFilter(result)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewPager = requireActivity().findViewById(R.id.viewpager)
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation_view)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configToolbar()
        if(binding.fragmentDetails != null){
            binding.recyclerViewListProperties.layoutManager = GridLayoutManager(context, 1)
        }
        else {
            binding.recyclerViewListProperties.layoutManager = GridLayoutManager(context, 2)
        }
        configPropertiesRv()
    }

    private fun configPropertiesRv(){
        viewModel
            .getAllProperties()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.recyclerViewListProperties.adapter = PropertyRvAdapter(it)
            }
    }

    @Subscribe
    fun onEvent(event: LaunchActivityEvent) {
        if (binding.fragmentDetails == null) {
            val intent = Intent(activity, PropertyActivity::class.java)
            intent.putExtra("REF", event.ref)
            startActivity(intent)
        }
    }

    private fun getResultToFilter(resultQuery: String) {
        if (resultQuery.isNotEmpty())
            viewModel.getPropertiesWithFilter(SimpleSQLiteQuery(resultQuery))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    binding.recyclerViewListProperties.adapter = PropertyRvAdapter(it)
                }
        else {
            viewModel.getAllProperties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    binding.recyclerViewListProperties.adapter = PropertyRvAdapter(it)
                }
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

    private fun configToolbar() {
        with(binding){
            toolbar.setOnMenuItemClickListener{
                when (it.itemId) {
                    R.id.home_top_filter -> {
                        configModalBottomSheet()
                    }
                }
                true
            }
        }
    }

    private fun configModalBottomSheet(){
        val modalBottomSheet = ModalBottomSheet()
        modalBottomSheet.show(parentFragmentManager, ModalBottomSheet.TAG)
    }
}