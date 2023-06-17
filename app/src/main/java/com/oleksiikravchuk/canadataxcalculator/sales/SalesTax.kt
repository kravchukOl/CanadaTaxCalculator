package com.oleksiikravchuk.canadataxcalculator.sales

import com.oleksiikravchuk.canadataxcalculator.models.Province
import java.lang.Math.round
import kotlin.math.floor

object SalesTax {

    fun getSalesTaxList(
        baseAmount: Double,
        province: Province
    ): List<Pair<Province.SaleTaxesType, Double>> {
        val taxesList = mutableListOf<Pair<Province.SaleTaxesType, Double>>()
        for (item in province.salesTaxRates) {
            taxesList.add(Pair(item.first, roundToCents( baseAmount * item.second )))
        }
        return taxesList.toList()

    }

    fun getTotalAmount(baseAmount: Double, province: Province) : Double {
        var totalAmount = baseAmount
        for( item in this.getSalesTaxList(baseAmount, province)) {
            totalAmount += item.second
        }
        return totalAmount
    }

    private fun roundToCents( amount : Double ) = floor(amount * 100 + 0.5) / 100

}