package com.oleksiikravchuk.canadataxcalculator.models

class Province(
    val provinceName: String,
    val imageId : Int,
    val provinceTaxRates: Array<Pair<Int,Double>>,
    val provinceTaxCredit : Int,
    val eligibleTaxCreditRate : Double,
    val nonEligibleTaxCreditRate : Double
) : java.io.Serializable