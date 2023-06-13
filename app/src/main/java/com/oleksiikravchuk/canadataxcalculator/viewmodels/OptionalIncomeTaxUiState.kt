package com.oleksiikravchuk.canadataxcalculator.viewmodels


data class OptionalIncomeTaxUiState(
    var totalTaxableIncome: Double = 0.0,
    var surtax: Double = 0.0,
    var capitalGainsTax: Double = 0.0,
    var rrspRefund: Double = 0.0,
    var eligibleDividendsTax: Double = 0.0,
    var nonEligibleDividendsTax: Double = 0.0,
    var deductionEI: Double = 0.0,
    var contributionCPP: Double = 0.0,
) {}