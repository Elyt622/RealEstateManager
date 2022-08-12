package com.openclassrooms.realestatemanager.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.databinding.ActivityLoanSimulatorBinding
import com.openclassrooms.realestatemanager.viewmodel.LoanSimulatorViewModel

class LoanSimulatorActivity : BaseActivity() {

    private lateinit var binding : ActivityLoanSimulatorBinding

    private lateinit var viewModel : LoanSimulatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanSimulatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoanSimulatorViewModel::class.java]

        configToolbar()
        configAllFieldsOnStart()

        with(binding) {
            buttonCalculate.setOnClickListener {
                try {
                    viewModel.checkData(
                        editTextAmountLoan.text.toString().toIntOrNull(),
                        textViewDurationLoan.text.toString().toIntOrNull(),
                        editTextPercentageInterest.text.toString().toDoubleOrNull(),
                        editTextPercentageInsurance.text.toString().toDoubleOrNull()
                    )
                    textViewMonthlyPayment.text = viewModel.getCalculationMortgagePayment()
                    showPaymentMessage(false)
                } catch (e: Exception) {
                    when(e.message) {
                        "LOAN_IS_NULL" ->
                            showToastMessage("Amount of the loan is empty")

                        "LOAN_INCORRECT_VALUE" ->
                            showToastMessage("Loan must be greater than 20000")

                        "DURATION_INCORRECT_VALUE" ->
                            showToastMessage("Duration must be greater than 10")

                        "INTEREST_INCORRECT_VALUE" ->
                            showToastMessage("Interest must be greater than 0")
                    }
                    showPaymentMessage(true)
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

    private fun showPaymentMessage(hide: Boolean) {
        with(binding) {
            if (!hide) {
                textViewCreditCost.text = viewModel.df.format(viewModel.interestGlobalToPay)
                textViewInsuranceCost.text = viewModel.df.format(viewModel.insuranceGlobalToPay)
                linearLayoutCreditCost.isGone = hide
            } else
                linearLayoutCreditCost.isGone = hide
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun configAllFieldsOnStart(){
        binding.textViewDurationLoan.text = viewModel.duration.toString()
        binding.textViewMonthlyPayment.text = viewModel.df.format(viewModel.interestMonthlyToPay)
        binding.editTextAmountLoan.setText(viewModel.loan.toString())
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