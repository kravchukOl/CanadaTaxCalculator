package com.oleksiikravchuk.canadataxcalculator.models

import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax

class Province(
    val provinceName: String,
    val imageId: Int,
    val provinceTaxRates: Array<Pair<Int, Double>>,
    val provinceTaxCredit: Int,
    val eligibleTaxCreditRate: Double,
    val nonEligibleTaxCreditRate: Double,
    var salesTaxRates: Array<Pair<SalesTax.SaleTaxesType,Double>>
) : java.io.Serializable {

}

