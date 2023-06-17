package com.oleksiikravchuk.canadataxcalculator.income

import com.google.common.truth.Truth
import org.junit.Before

import org.junit.Test

class OptionalTaxesTest {

    private lateinit  var tax : OptionalTaxes

    @Before
    fun setup(){
        tax = OptionalTaxes()
    }


    @Test
    fun getCapitalGainsTax_Input_Gains_5000_MarginalRate_0407() {
        val rate = tax.getCapitalGainsTax(5000.0, 0.407)
        Truth.assertThat(rate).isWithin(0.1e-10).of(1017.50)
    }
}