package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import com.openclassrooms.realestatemanager.databinding.ActivityLoanSimulatorBinding


class LoanSimulatorActivity : BaseActivity() {

    private lateinit var binding : ActivityLoanSimulatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanSimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}