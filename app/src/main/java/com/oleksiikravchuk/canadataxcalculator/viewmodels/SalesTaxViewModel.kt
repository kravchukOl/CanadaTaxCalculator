package com.oleksiikravchuk.canadataxcalculator.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax

class SalesTaxViewModel : ViewModel() {

    var basePrice = 0.0
        set(value) {
            field = value
            if (value > 0) {
                calculateTaxes()
            }
        }

    var discount = 0.0
        set(value) {
            field = value
            if (field > 0) {
                calculateTaxes()
            }
        }

    lateinit var selectedProvince: Province

    val saleItemUiState: MutableLiveData<SaleItemUiState> = MutableLiveData()


    private fun calculateTaxes() {
        val subTotal = basePrice - (basePrice * discount / 100)
        val taxList = SalesTax.getSalesTaxList(subTotal, selectedProvince)
        val total = SalesTax.getTotalAmount(subTotal,selectedProvince)

        saleItemUiState.value = SaleItemUiState(basePrice, discount, taxList, total)
    }

    fun setProvince(province: Province) {
        this.selectedProvince = province
        if (basePrice > 0)
            calculateTaxes()
    }

}