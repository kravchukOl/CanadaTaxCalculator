package com.oleksiikravchuk.canadataxcalculator.viewmodels

import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax

data class SaleUiState(
    val subTotal: Double,
    val discount: Double,
    val taxesList : List<Pair<SalesTax.SaleTaxesType, Double>>,
    val total : Double
) {}
