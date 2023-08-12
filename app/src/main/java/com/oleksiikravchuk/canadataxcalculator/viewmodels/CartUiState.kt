package com.oleksiikravchuk.canadataxcalculator.viewmodels

import com.oleksiikravchuk.canadataxcalculator.models.CartItem

class CartUiState  {
    val salesItemList  = mutableListOf<CartItem>()
    var subTotal: Double = 0.0
    var taxes: Double = 0.0
    var total: Double = 0.0

    fun addItem( cartItem: CartItem) {
        salesItemList.add(cartItem)
        subTotal += cartItem.subTotal
    }
}