package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

class LoanSimulatorViewModel : ViewModel() {

    private var duration : Int = 15

    private var interestRate : Double = 1.65

    private var insuranceRate : Double = 0.34

    private var insuranceGlobalToPay : Double = 0.0

    private var interestGlobalToPay : Double = 0.0

    fun getDuration() = duration

    fun getInterestRate() = interestRate

    fun getInsuranceRate() = insuranceRate

    fun getInsuranceGlobalToPay() = insuranceGlobalToPay

    fun getInterestGlobalToPay() = interestGlobalToPay

    fun setDuration(duration: Int){
        this.duration = duration
    }

    private fun setInterestRate(interestRate: Double) {
        this.interestRate = interestRate
    }

    private fun setInsuranceRate(insuranceRate: Double) {
        this.insuranceRate = insuranceRate
    }

    private fun setInsuranceGlobalToPay(insuranceGlobalToPay: Double) {
        this.insuranceGlobalToPay = insuranceGlobalToPay
    }

    private fun setInterestGlobalToPay(interestGlobalToPay: Double) {
        this.interestGlobalToPay = interestGlobalToPay
    }

    fun getCalculationMortgagePayment(
        loan: Double,
    ): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP

        val insuranceGlobalToPay = (insuranceRate / 100.0 * loan * duration)
        setInsuranceGlobalToPay(insuranceGlobalToPay)

        val monthlyToPay =
            ((loan * interestRate / 100) / 12) / (1 - (1 + (interestRate / 100 / 12)).pow(-duration * 12))
        val interestGlobalToPay = ((12.0 * duration * monthlyToPay) - loan)
        setInterestGlobalToPay(interestGlobalToPay)

        val insuranceMonthlyToPay = getInsuranceGlobalToPay() / (duration * 12)

        return df.format(monthlyToPay + insuranceMonthlyToPay)
    }

    fun setDefaultData(
        duration: Int?,
        interestRate: Double?,
        insuranceRate: Double?
    ) {
        if (duration == null) setDuration(15) else setDuration(duration)
        if (interestRate == null) setInterestRate(1.65) else setInterestRate(interestRate)
        if (insuranceRate == null) setInsuranceRate(0.34) else setInsuranceRate(insuranceRate)
    }

}