package com.oleksiikravchuk.canadataxcalculator.income

import com.oleksiikravchuk.canadataxcalculator.models.Province

class OptionalTaxes {

    // Capital Gain Rates:
    private val inclusionRate = 0.5

    // Dividends Rates:
    val eligibleGrossUpRate = 1.38
    val nonEligibleGrossUpRate = 1.15

    val federalEligibleTaxCreditRate = 0.150198
    val federalNonEligibleTaxCreditRate = 0.090301

    fun getCapitalGainsTax(capitalGains: Double, marginalRate: Double): Double {
        return if (capitalGains <= 0)
            0.0
        else
            (capitalGains * inclusionRate) * marginalRate
    }

    fun getCapitalGainsTaxableIncome(capitalGains: Double): Double {
        return if (capitalGains <= 0)
            0.0
        else
            capitalGains * inclusionRate
    }

    fun getEligibleDivsGrossUpIncome(amount: Double): Double {
        return if (amount <= 0) {
            0.0
        } else {
            amount * this.eligibleGrossUpRate
        }
    }

    fun getNonEligibleDivsGrossUpIncome(amount: Double): Double {
        return if (amount <= 0) {
            0.0
        } else {
            amount * this.nonEligibleGrossUpRate
        }
    }

    fun getTaxCreditOnNonEligibleDivs(amount: Double, province: Province): Double {
        val federalCredit =
            getNonEligibleDivsGrossUpIncome(amount) * this.federalNonEligibleTaxCreditRate
        val provincialCredit =
            getNonEligibleDivsGrossUpIncome(amount) * province.nonEligibleTaxCreditRate

        return federalCredit + provincialCredit
    }

    fun getTaxCreditOnEligibleDivs(amount: Double, province: Province): Double {
        val provincialCredit =
            getEligibleDivsGrossUpIncome(amount) * this.federalEligibleTaxCreditRate
        val federalCredit =
            getEligibleDivsGrossUpIncome(amount) * province.eligibleTaxCreditRate

        return federalCredit + provincialCredit
    }

    fun getEligibleDivsTax(amount: Double, province: Province, marginalRate: Double): Double {
        val grossUpIncome = getEligibleDivsGrossUpIncome(amount)
        val credit = getTaxCreditOnEligibleDivs(amount, province)
        return grossUpIncome * marginalRate - credit
    }

    fun getNonEligibleDivsTax(amount: Double, province: Province, marginalRate: Double): Double {
        val grossUpIncome = getNonEligibleDivsGrossUpIncome(amount)
        val credit = getTaxCreditOnNonEligibleDivs(amount, province)
        return grossUpIncome * marginalRate - credit
    }
}
