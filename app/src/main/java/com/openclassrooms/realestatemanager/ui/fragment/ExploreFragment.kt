package com.openclassrooms.realestatemanager.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.databinding.ExploreFragmentBinding
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.ui.adapter.OptionRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.ui.adapter.StatusRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.ui.adapter.TypeRvAdapterExploreFragment
import com.openclassrooms.realestatemanager.viewmodel.ExploreViewModel

class ExploreFragment : Fragment() {

    private lateinit var binding: ExploreFragmentBinding

    companion object {
        fun newInstance() = ExploreFragment()
    }

    private lateinit var viewModel: ExploreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExploreFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ExploreViewModel::class.java]
        configTypeRv()
        configOptionRv()
        configStatusRv()
        // TODO: Use the ViewModel
    }

    private fun configTypeRv(){
        with(binding){
            recyclerViewType.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewType.adapter = TypeRvAdapterExploreFragment(
                viewModel,
                context,
                Type.values()
            )
        }
    }

    private fun configOptionRv(){
        with(binding){
            recyclerViewOptions.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewOptions.adapter = OptionRvAdapterExploreFragment(
                viewModel,
                context,
                Option.values()
            )
        }
    }

    private fun configStatusRv(){
        with(binding){
            recyclerViewStatus.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerViewStatus.adapter = StatusRvAdapterExploreFragment(
                viewModel,
                context,
                Status.values()
            )
        }
    }

}