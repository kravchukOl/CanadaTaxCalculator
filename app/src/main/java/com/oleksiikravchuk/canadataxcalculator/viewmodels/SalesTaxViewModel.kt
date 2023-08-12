package com.oleksiikravchuk.canadataxcalculator.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.models.CartItem
import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax

class SalesTaxViewModel : ViewModel() {

    var basePrice = 0.0
        set(value) {
            field = value
            if (field >= 0) {
                calculateTaxes()
            }
        }

    var discount = 0.0
        set(value) {
            field = value
            if (field >= 0) {
                calculateTaxes()
            }
        }

    lateinit var selectedProvince: Province

    private val _saleItemUiState: MutableLiveData<SaleItemUiState> = MutableLiveData()
    val saleItemUiState: LiveData<SaleItemUiState>
        get() = _saleItemUiState

    private val cartList = mutableListOf<CartItem>()

//    val cartListUiState : LiveData<List<CartItem>>
//        get() {
//
//        }


    private fun calculateTaxes() {
        val subTotal = basePrice - (basePrice * discount / 100)
        val taxList = SalesTax.getSalesTaxList(subTotal, selectedProvince)
        val total = SalesTax.getTotalAmount(subTotal, selectedProvince)

        _saleItemUiState.value = SaleItemUiState(basePrice, discount, taxList, total)
    }

    fun setProvince(province: Province) {
        this.selectedProvince = province
        if (basePrice > 0)
            calculateTaxes()
    }

}