package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.HomeFragmentBinding
import com.openclassrooms.realestatemanager.event.LaunchActivityEvent
import com.openclassrooms.realestatemanager.ui.activity.PropertyActivity
import com.openclassrooms.realestatemanager.ui.adapter.PropertyRvAdapter
import com.openclassrooms.realestatemanager.viewmodel.HomeViewModel
import com.openclassrooms.realestatemanager.viewmodel.ViewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: HomeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestRef") { _, bundle ->
            val result = bundle.getInt("RefBundle")
            EventBus.getDefault().post(LaunchActivityEvent(result))
        }
        setFragmentResultListener("requestSqlToList") { _, bundle ->
            val result = bundle.getString("SqlListBundle")
            if (result != null) {
                getResultToFilter(result)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(layoutInflater)
        viewModelFactory = ViewModelFactory(propertyDao)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

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
                    if (it.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "No result with this filter",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        binding.recyclerViewListProperties.adapter = PropertyRvAdapter(it)
                        if(requireContext().resources.configuration.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
                            EventBus.getDefault().post(LaunchActivityEvent(it[0].ref))
                    }
                }
        else {
            viewModel.getAllProperties()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    binding.recyclerViewListProperties.adapter = PropertyRvAdapter(it)
                    if(requireContext().resources.configuration.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
                        EventBus.getDefault().post(LaunchActivityEvent(it[0].ref))
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
        val modalBottomFragment = ModalBottomFragment()
        modalBottomFragment.show(parentFragmentManager, ModalBottomFragment.TAG)
    }
}