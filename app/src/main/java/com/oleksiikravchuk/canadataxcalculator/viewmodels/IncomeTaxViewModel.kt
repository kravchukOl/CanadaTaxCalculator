package com.oleksiikravchuk.canadataxcalculator.viewmodels

import androidx.lifecycle.LiveData
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

    lateinit var selectedProvince: Province
    var basicIncome: Double = 0.0
    var contributionRRSP: Double = 0.0
    var capitalGains: Double = 0.0
    var eligibleDividends: Double = 0.0
    var nonEligibleDividends: Double = 0.0

    var isSelfEmployed: Boolean = false
    var isEiIncluded = true
    var isCppIncluded = true

    var containsData: Boolean = false
        private set


    private val _mainIncomeTaxUiState: MutableLiveData<MainIncomeTaxUiState> = MutableLiveData()
    val mainIncomeTaxUiStates: LiveData<MainIncomeTaxUiState>
        get() = _mainIncomeTaxUiState

    private val _totalActualIncome: MutableLiveData<Double> = MutableLiveData()
    val totalActualIncome: LiveData<Double>
        get() = _totalActualIncome

    private val _totalTaxableIncome: MutableLiveData<Double> = MutableLiveData()
    val totalTaxableIncome: LiveData<Double>
        get() = _totalTaxableIncome

    private val _provinceSurtax: MutableLiveData<Double> = MutableLiveData()
    val provinceSurtax: LiveData<Double>
        get() = _provinceSurtax

    private val _capitalGainsTax: MutableLiveData<Double> = MutableLiveData()
    val capitalGainsTax: LiveData<Double>
        get() = _capitalGainsTax

    private val _rrspRefund: MutableLiveData<Double> = MutableLiveData()
    val  rrspRefund: LiveData<Double>
        get() = _rrspRefund

    private val _eligibleDividendsTax: MutableLiveData<Double> = MutableLiveData()
    val  eligibleDividendsTax: LiveData<Double>
        get() = _eligibleDividendsTax

    private val _nonEligibleDividendsTax: MutableLiveData<Double> = MutableLiveData()
    val  nonEligibleDividendsTax : LiveData<Double>
        get() = _nonEligibleDividendsTax

    private val _deductionEI: MutableLiveData<Double> = MutableLiveData()
    val  deductionEI : LiveData<Double>
        get() = _deductionEI

    private val _contributionCPP: MutableLiveData<Double> = MutableLiveData()
    val contributionCPP : LiveData<Double>
        get() = _contributionCPP



    fun calculate() {
        this.containsData = true

        val totalTaxableIncome = getTotalTaxableIncome()
        this._totalTaxableIncome.value = totalTaxableIncome
        this._totalActualIncome.value = getTotalActualIncome()

        val federalTax = federal.getFederalTax(totalTaxableIncome, this.selectedProvince)

        val provincialTax = provincial.getProvinceTax(totalTaxableIncome, this.selectedProvince)

        val surtax = getSurtax(provincialTax, selectedProvince)
        if (surtax > 0.0) {
            this._provinceSurtax.value = surtax
        }

        val totalIncomeTax = federalTax + provincialTax - dividendCredits()

        var marginalTaxRate = federal.getMarginalTaxRate(totalTaxableIncome, selectedProvince)
        marginalTaxRate += provincial.getMarginalTaxRate(totalTaxableIncome, selectedProvince)

        val averageTaxRate = totalIncomeTax / getTotalActualIncome()

        if (this.capitalGains > 0) {
            this._capitalGainsTax.value =
                this.optionalTaxes.getCapitalGainsTax(capitalGains, marginalTaxRate)
        }

        if (this.eligibleDividends > 0) {
            _eligibleDividendsTax.value =
                this.optionalTaxes.getEligibleDivsTax(
                    eligibleDividends,
                    selectedProvince,
                    marginalTaxRate
                )
        }

        if (this.nonEligibleDividends > 0) {
            this._nonEligibleDividendsTax.value =
                this.optionalTaxes.getNonEligibleDivsTax(
                    nonEligibleDividends,
                    selectedProvince,
                    marginalTaxRate
                )
        }

        var deductionEI = 0.0
        if (this.isEiIncluded) {
            deductionEI =
                deductions.getEmploymentInsuranceDeduction(totalTaxableIncome, selectedProvince)
            this._deductionEI.value = deductionEI
        }

        var contributionCpp = 0.0
        if (this.isCppIncluded) {
            contributionCpp = deductions.getCanadaPensionPlanContribution(
                totalTaxableIncome,
                this.selectedProvince,
                this.isSelfEmployed
            )
            this._contributionCPP.value = contributionCpp
        }
        val totalNetIncome = totalTaxableIncome - totalIncomeTax - contributionCpp - deductionEI



        if (contributionRRSP > 0) {
            _rrspRefund.value = contributionRRSP * marginalTaxRate
        }

        _mainIncomeTaxUiState.value = MainIncomeTaxUiState(
            totalNetIncome,
            totalIncomeTax,
            federalTax,
            provincialTax,
            marginalTaxRate * 100,
            averageTaxRate * 100
        )
    }


    private fun getTotalTaxableIncome(): Double {
        var income = this.basicIncome
        income += this.optionalTaxes.getCapitalGainsTaxableIncome(this.capitalGains)
        income += this.optionalTaxes.getEligibleDivsGrossUpIncome(this.eligibleDividends)
        income += this.optionalTaxes.getNonEligibleDivsGrossUpIncome(this.nonEligibleDividends)
        return income
    }

    private fun getTotalActualIncome(): Double {
        var income = this.basicIncome
        income += this.capitalGains
        income += this.eligibleDividends
        income += this.nonEligibleDividends
        return income
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

    private fun dividendCredits() =
        optionalTaxes.getTaxCreditOnEligibleDivs(
            eligibleDividends,
            selectedProvince
        ) + optionalTaxes.getTaxCreditOnNonEligibleDivs(nonEligibleDividends, selectedProvince)
}