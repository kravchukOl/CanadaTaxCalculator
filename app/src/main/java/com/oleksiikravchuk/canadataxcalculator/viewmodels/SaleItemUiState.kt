package com.oleksiikravchuk.canadataxcalculator.viewmodels

import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax

data class SaleItemUiState(
    val subTotal: Double,
    val discount: Double,
    val taxesList : List<Pair<SalesTax.SaleTaxesType, Double>>,
    val total : Double
) {}
