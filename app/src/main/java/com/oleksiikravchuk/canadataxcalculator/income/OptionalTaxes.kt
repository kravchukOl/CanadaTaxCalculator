package com.oleksiikravchuk.canadataxcalculator.income

import com.oleksiikravchuk.canadataxcalculator.models.Province

class OptionalTaxes {

    private val inclusionRate = 0.5

    // Dividends Rates:

    val eligibleGrossUpRate = 1.38
    val nonEligibleGrossUpRate = 1.15

    val federalEligibleTaxCreditRate = 0.150198
    val federalNonEligibleTaxCreditRate = 0.90301

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

    fun getEligibleDividendsTaxableIncome(actualDividends: Double, province: Province): Double {
        if (actualDividends <= 0) {
            return 0.0
        }
        var income = actualDividends * this.eligibleGrossUpRate
        income -= income * federalEligibleTaxCreditRate
        income -= income * province.eligibleTaxCreditRate
        return income
    }

    fun getNonEligibleDividendsTaxableIncome(actualDividends: Double, province: Province): Double {
        if (actualDividends <= 0) {
            return 0.0
        }
        var income = actualDividends * this.nonEligibleGrossUpRate
        income -= income * federalNonEligibleTaxCreditRate
        income -= income * province.nonEligibleTaxCreditRate
        return income
    }

    fun getDividendsTax(taxableDividends: Double, marginalRate: Double) = taxableDividends * marginalRate


}
