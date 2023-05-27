package com.oleksiikravchuk.canadataxcalculator.income

import com.google.common.truth.Truth
import org.junit.Assert
import org.junit.Assert.*
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
        Truth.assertThat(tax.getCapitalGainsTax(5000.0, 0.407)).isAtMost(1017.50)
        Truth.assertThat(tax.getCapitalGainsTax(5000.0, 0.407)).isAtLeast(1017.49)
    }
}