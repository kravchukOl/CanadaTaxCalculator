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

    private var selectedProvince = provincial.provincesAndRates2023[0];
    private var basicIncome: Double = 0.0
    private var contributionRRCP: Double = 0.0
    private var capitalGains: Double = 0.0
    private var eligibleDividends: Double = 0.0
    private var nonEligibleDividends: Double = 0.0

    private var isSelfEmployed: Boolean = true
    private var isEiIncluded = true
    private var isCppIncluded = true

    private var totalTaxableIncome: MutableLiveData<Double> = MutableLiveData()
    private var totalNetIncome: MutableLiveData<Double> = MutableLiveData()

    private var federalTax: MutableLiveData<Double> = MutableLiveData()
    private var provincialTax: MutableLiveData<Double> = MutableLiveData()
    private var provinceSurtax: MutableLiveData<Double> = MutableLiveData()
    private var capitalGainsTax: MutableLiveData<Double> = MutableLiveData()
    private var totalIncomeTax: MutableLiveData<Double> = MutableLiveData()
    private var eligibleDividendsTax: MutableLiveData<Double> = MutableLiveData()
    private var nonEligibleDividendsTax: MutableLiveData<Double> = MutableLiveData()

    private var deductionEI: MutableLiveData<Double> = MutableLiveData()
    private var contributionCPP: MutableLiveData<Double> = MutableLiveData()

    private var marginalTaxRate: MutableLiveData<Double> = MutableLiveData()
    private var averageTaxRate: MutableLiveData<Double> = MutableLiveData()


    fun calculate() {
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
        this.marginalTaxRate.value = marginalTaxRate

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
        return basicIncome + (capitalGains * 0.5) - contributionRRCP
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