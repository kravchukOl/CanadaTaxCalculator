package com.oleksiikravchuk.canadataxcalculator.sales

data class CartItem(
    val subTotal: Double,
    val taxes: Double,
    val total: Double,
    var tag: String = ""
) {}