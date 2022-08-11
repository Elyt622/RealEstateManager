package com.openclassrooms.realestatemanager.viewmodel

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoanSimulatorViewModelTests {

    lateinit var viewModel : LoanSimulatorViewModel

    @Before
    fun setup(){
        viewModel = LoanSimulatorViewModel()
    }

    @Test
    fun getCalculationMortgagePaymentTest(){
        viewModel.duration = 15
        val loanValue = 100000.0
        viewModel.interestRate = 1.65
        viewModel.insuranceRate = 0.34
        val expectedGlobalInsurance = 5100.0
        val expectedGlobalInterest = 12953.333052534188
        val expectedMonthlyInsurance = 28.333333333333332
        val expectedMonthlyInterest = 655.8518502918566

        viewModel.getCalculationMortgagePayment(loanValue)

        Assert.assertEquals(viewModel.insuranceGlobalToPay, expectedGlobalInsurance, 0.0)
        Assert.assertEquals(viewModel.interestGlobalToPay, expectedGlobalInterest, 0.0)
        Assert.assertEquals(viewModel.interestMonthlyToPay, expectedMonthlyInterest, 0.0)
        Assert.assertEquals(viewModel.insuranceMonthlyToPay, expectedMonthlyInsurance, 0.0)
    }
}