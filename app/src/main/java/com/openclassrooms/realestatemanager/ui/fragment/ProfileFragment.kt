package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ProfileFragmentBinding
import com.openclassrooms.realestatemanager.ui.activity.AddPropertyActivity
import com.openclassrooms.realestatemanager.ui.activity.LoanSimulatorActivity
import com.openclassrooms.realestatemanager.viewmodel.ProfileViewModel

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        with(binding) {
            buttonAddProperty.setOnClickListener {
                val intent = Intent(context, AddPropertyActivity::class.java)
                startActivity(intent)
            }

            buttonLoanSimulator.setOnClickListener {
                val intent = Intent(context, LoanSimulatorActivity::class.java)
                startActivity(intent)
            }
        }
    }
}