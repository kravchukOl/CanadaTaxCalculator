package com.oleksiikravchuk.canadataxcalculator.income

import com.oleksiikravchuk.canadataxcalculator.models.Province

class Deductions {

    private val federalEmploymentInsuranceRates2023 = Pair(61500, 0.0163)
    private val quebecEmploymentInsuranceRates2023 = Pair(61500, 0.0127)

    private val canadaPensionPlanRates2023 = Pair(63100, 0.0595)
    private val quebecPensionPlanRates2023 = Pair(66600, 0.054)
    private val basicExemptionAmountCPP = 3500

    fun getEmploymentInsuranceDeduction(annualIncome: Double, province: Province): Double {
        val ratesEI = if (province.provinceName == "Quebec")
            quebecEmploymentInsuranceRates2023
        else federalEmploymentInsuranceRates2023

        return if (annualIncome >= ratesEI.first)
            ratesEI.first * ratesEI.second
        else
            annualIncome * ratesEI.second
    }

    fun getCanadaPensionPlanContribution(
        annualIncome: Double,
        province: Province,
        isSelfEmployed: Boolean = false
    ): Double {

        if(annualIncome <= basicExemptionAmountCPP)
            return 0.0

        var contributionRate: Double
        val maxAmountContributeOn: Int

        if (province.provinceName == "Quebec") {
            contributionRate = quebecPensionPlanRates2023.second
            maxAmountContributeOn = quebecPensionPlanRates2023.first
        } else {
            contributionRate = canadaPensionPlanRates2023.second
            maxAmountContributeOn = canadaPensionPlanRates2023.first
        }

        if (isSelfEmployed) contributionRate *= 2

        return if (annualIncome >= maxAmountContributeOn)
            maxAmountContributeOn * contributionRate
        else
            annualIncome * contributionRate
    }

}