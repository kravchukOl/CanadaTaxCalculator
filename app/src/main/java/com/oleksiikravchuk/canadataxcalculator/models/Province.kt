package com.oleksiikravchuk.canadataxcalculator.models

class Province(
    val provinceName: String,
    val imageId : Int,
    val provinceTaxRates: Array<Pair<Int,Double>>,
    val provinceTaxCredit : Int
) : java.io.Serializable