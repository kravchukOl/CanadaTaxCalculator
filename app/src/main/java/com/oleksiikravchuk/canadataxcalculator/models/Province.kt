package com.oleksiikravchuk.canadataxcalculator.models

class Province(
    val provinceName: String,
    val imageId: Int,
    val provinceTaxRates: Array<Pair<Int, Double>>,
    val provinceTaxCredit: Int,
    val eligibleTaxCreditRate: Double,
    val nonEligibleTaxCreditRate: Double,
    var salesTaxRates: Array<Pair<SaleTaxesType,Double>>
) : java.io.Serializable {
    enum class SaleTaxesType {
        HST,
        GST,
        PST
    }
}

