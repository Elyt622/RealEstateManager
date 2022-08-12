package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

class LoanSimulatorViewModel : ViewModel() {

    var loan : Int = 20000

    var duration : Int = 15

    var interestRate : Double = 1.65

    var insuranceRate : Double = 0.34

    var insuranceGlobalToPay : Double = 0.0

    var interestGlobalToPay : Double = 0.0

    var interestMonthlyToPay : Double = 0.0

    var insuranceMonthlyToPay : Double = 0.0

    val df = DecimalFormat("#.##")

    init {
        df.roundingMode = RoundingMode.UP
        checkData(loan, duration, interestRate, insuranceRate)
    }

    fun getCalculationMortgagePayment(): String {

        val insuranceGlobalToPay = (insuranceRate / 100.0 * loan * duration)
        this.insuranceGlobalToPay = insuranceGlobalToPay

        val monthlyToPay =
            ((loan * interestRate / 100) / 12) / (1 - (1 + (interestRate / 100 / 12)).pow(-duration * 12))
        val interestGlobalToPay = ((12.0 * duration * monthlyToPay) - loan)
        this.interestGlobalToPay = interestGlobalToPay

        insuranceMonthlyToPay = insuranceGlobalToPay / (duration * 12)

        interestMonthlyToPay = monthlyToPay + insuranceMonthlyToPay
        return df.format(interestMonthlyToPay)
    }

    fun checkData(
        loan: Int?,
        duration: Int?,
        interestRate: Double?,
        insuranceRate: Double?
    ) {
        if (loan == null) throw Exception("LOAN_IS_NULL")
        else if (loan < 20000) throw Exception("LOAN_INCORRECT_VALUE")
        else this.loan = loan

        if (duration == null) this.duration = 15
        else if (duration < 10) throw Exception("DURATION_INCORRECT_VALUE")
        else this.duration = duration

        if (interestRate == null) this.interestRate = 1.65
        else if (interestRate <= 0) throw Exception("INTEREST_INCORRECT_VALUE")
        else this.interestRate = interestRate

        if (insuranceRate == null) this.insuranceRate = 0.34
        else this.insuranceRate = insuranceRate
    }

}