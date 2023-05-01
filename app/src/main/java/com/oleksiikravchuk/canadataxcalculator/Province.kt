package com.oleksiikravchuk.canadataxcalculator

class Province(
    val provinceName: String,
    val imageId : Int,
    val individualsIncomeTaxRates: Array<Pair<Int,Double>>
) : java.io.Serializable