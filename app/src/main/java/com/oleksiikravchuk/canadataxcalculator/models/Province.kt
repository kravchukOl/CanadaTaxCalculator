package com.oleksiikravchuk.canadataxcalculator.models

class Province(
    val provinceName: String,
    val imageId : Int,
    val individualsIncomeTaxRates: Array<Pair<Int,Double>>,
    val personalTaxCredit : Int
) : java.io.Serializable