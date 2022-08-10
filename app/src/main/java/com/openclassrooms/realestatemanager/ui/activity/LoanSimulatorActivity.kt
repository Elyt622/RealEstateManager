package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivityLoanSimulatorBinding
import com.openclassrooms.realestatemanager.viewmodel.LoanSimulatorViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class LoanSimulatorActivity : BaseActivity() {

    private lateinit var binding : ActivityLoanSimulatorBinding

    private lateinit var viewModel : LoanSimulatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanSimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoanSimulatorViewModel::class.java]

        configToolbar()

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP

        binding.textViewDurationLoan.text = viewModel.duration.toString()
        binding.textViewMonthlyPayment.text = df.format(viewModel.interestMonthlyToPay)

        with(binding) {
            buttonCalculate.setOnClickListener {

                viewModel.setDefaultData(
                    textViewDurationLoan.text.toString().toIntOrNull(),
                    editTextPercentageInterest.text.toString().toDoubleOrNull(),
                    editTextPercentageInsurance.text.toString().toDoubleOrNull()
                )

                if (editTextAmountLoan.text.toString().isEmpty()) {
                    Toast.makeText(
                        this@LoanSimulatorActivity,
                        "Amount of the loan is empty",
                        Toast.LENGTH_LONG
                    ).show()
                    linearLayoutCreditCost.isGone = true
                } else {
                    textViewMonthlyPayment.text = viewModel.getCalculationMortgagePayment(
                        editTextAmountLoan.text.toString().toDouble()
                    )
                    textViewCreditCost.text = df.format(viewModel.interestGlobalToPay)
                    textViewInsuranceCost.text = df.format(viewModel.insuranceGlobalToPay)
                    linearLayoutCreditCost.isGone = false
                }
            }

            imageButtonAddYears.setOnClickListener {
                viewModel.duration = viewModel.duration + 1
                textViewDurationLoan.text = viewModel.duration.toString()
            }

            imageButtonRemoveYears.setOnClickListener {
                viewModel.duration = viewModel.duration - 1
                textViewDurationLoan.text = viewModel.duration.toString()
            }
        }
    }

    private fun configToolbar() {
        setSupportActionBar(binding.toolbarTop)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}