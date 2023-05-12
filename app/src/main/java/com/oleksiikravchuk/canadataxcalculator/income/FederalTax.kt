package com.oleksiikravchuk.canadataxcalculator.income

import com.oleksiikravchuk.canadataxcalculator.models.Province

class FederalTax {
    private val federalTaxBrackets2023 = arrayOf(
        Pair(0, 0.15),
        Pair(53359, 0.205),
        Pair(106717, 0.26),
        Pair(165430, 0.29),
        Pair(235675, 0.33)
    )
    private val personaFederalTaxCredit2023: Int = 15000

    private val federalTaxReductionForQuebec = 0.165

    fun getFederalTax(annualIncome: Double, province: Province): Double {
        return if (province.provinceName == "Quebec") {
            val commonFederalTax = calculateTaxCommonRates(
                annualIncome,
                federalTaxBrackets2023,
                personaFederalTaxCredit2023
            )
            commonFederalTax - commonFederalTax * federalTaxReductionForQuebec
        } else {
            calculateTaxCommonRates(
                annualIncome,
                federalTaxBrackets2023,
                personaFederalTaxCredit2023
            )
        }
    }
}