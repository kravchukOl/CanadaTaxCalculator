package com.oleksiikravchuk.canadataxcalculator.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oleksiikravchuk.canadataxcalculator.income.Deductions
import com.oleksiikravchuk.canadataxcalculator.income.FederalTax
import com.oleksiikravchuk.canadataxcalculator.income.OptionalTaxes
import com.oleksiikravchuk.canadataxcalculator.income.ProvincialTax
import com.oleksiikravchuk.canadataxcalculator.models.Province

class IncomeTaxViewModel : ViewModel() {

    private val federal = FederalTax()
    private val provincial = ProvincialTax()
    private val deductions = Deductions()
    private var optionalTaxes = OptionalTaxes()

    var selectedProvince = provincial.provincesAndRates2023[0];
    var basicIncome: Double = 0.0
    var contributionRRCP: Double = 0.0
    var capitalGains: Double = 0.0
    var eligibleDividends: Double = 0.0
    var nonEligibleDividends: Double = 0.0

    var isSelfEmployed: Boolean = true
    var isEiIncluded = true
    var isCppIncluded = true

    var containsData: Boolean = false
        private set

    val totalTaxableIncome: MutableLiveData<Double> = MutableLiveData()
    val totalNetIncome: MutableLiveData<Double> = MutableLiveData()

    val federalTax: MutableLiveData<Double> = MutableLiveData()
    val provincialTax: MutableLiveData<Double> = MutableLiveData()
    val provinceSurtax: MutableLiveData<Double> = MutableLiveData()
    val capitalGainsTax: MutableLiveData<Double> = MutableLiveData()
    val totalIncomeTax: MutableLiveData<Double> = MutableLiveData()
    val eligibleDividendsTax: MutableLiveData<Double> = MutableLiveData()
    val nonEligibleDividendsTax: MutableLiveData<Double> = MutableLiveData()

    val deductionEI: MutableLiveData<Double> = MutableLiveData()
    val contributionCPP: MutableLiveData<Double> = MutableLiveData()

    val marginalTaxRate: MutableLiveData<Double> = MutableLiveData()
    val averageTaxRate: MutableLiveData<Double> = MutableLiveData()


    fun calculate() {
        this.containsData = true

        val totalTaxableIncome = getTotalTaxableIncome()
        this.totalTaxableIncome.value = totalTaxableIncome

        val federalTax = federal.getFederalTax(totalTaxableIncome, this.selectedProvince)
        this.federalTax.value = federalTax

        val provincialTax = provincial.getProvinceTax(totalTaxableIncome, this.selectedProvince)
        this.provincialTax.value = provincialTax

        val surtax = getSurtax(provincialTax, selectedProvince)
        if (surtax > 0.0)
            this.provinceSurtax.value = surtax

        val marginalTaxRate = federal.getMarginalTaxRate(
            totalTaxableIncome,
            selectedProvince
        ) + provincial.getMarginalTaxRate(totalTaxableIncome, selectedProvince)
        this.marginalTaxRate.value = marginalTaxRate * 100

        this.averageTaxRate.value = (provincialTax + federalTax) / totalTaxableIncome * 100

        if (capitalGains > 0) {
            this.capitalGainsTax.value =
                optionalTaxes.getCapitalGainsTax(capitalGains, marginalTaxRate)
        }

        val totalIncomeTax = federalTax + provincialTax
        this.totalIncomeTax.value = totalIncomeTax

        var deductionEI = 0.0
        if (this.isEiIncluded) {
            deductionEI =
                deductions.getEmploymentInsuranceDeduction(totalTaxableIncome, selectedProvince)
            this.deductionEI.value = deductionEI
        }

        var contributionCpp = 0.0
        if (this.isCppIncluded) {
            contributionCpp = deductions.getCanadaPensionPlanContribution(
                totalTaxableIncome,
                this.selectedProvince,
                this.isSelfEmployed
            )
            this.contributionCPP.value = contributionCpp
        }
        totalNetIncome.value =
            totalTaxableIncome - federalTax - provincialTax - contributionCpp - deductionEI
    }

    private fun getTotalTaxableIncome(): Double {
        val income = basicIncome + (capitalGains * 0.5) - contributionRRCP
        return if (income < 0)
            0.0
        else
            income
    }

    private fun getSurtax(provinceTax: Double, selectedProvince: Province): Double =
        when (selectedProvince.provinceName) {
            "Ontario" -> provincial.getSurtax(provinceTax, provincial.ontarioSurtaxRates2023)
            "Prince Edward Island" -> provincial.getSurtax(
                provinceTax,
                provincial.ontarioSurtaxRates2023
            )
            else -> 0.0
        }
}