package com.oleksiikravchuk.canadataxcalculator.models

data class CartItem(
    val subTotal: Double,
    val taxes: Double,
    val total: Double,
    var tag: String = ""
) {}