package com.openclassrooms.realestatemanager.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ProfileFragmentBinding
import com.openclassrooms.realestatemanager.ui.activity.AddPropertyActivity
import com.openclassrooms.realestatemanager.ui.activity.LoanSimulatorActivity
import com.openclassrooms.realestatemanager.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    private lateinit var textviewProfile: TextView

    private lateinit var binding: ProfileFragmentBinding

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(layoutInflater)
        viewPager = requireActivity().findViewById(R.id.viewpager_activity_main)

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

    override fun onResume() {
        super.onResume()
        viewPager.isUserInputEnabled = true
    }
}