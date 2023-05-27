package com.oleksiikravchuk.canadataxcalculator.income

import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before

import org.junit.Test

class FederalTaxTest {

    lateinit var federalTax : FederalTax

    private val provincialTax = ProvincialTax()

    @Before
    fun setUp()
    {
        federalTax = FederalTax()
    }

    @After
    fun tearDown(){

    }


    @Test
    fun getFederalTax_InputZero_ReturnZero() {
        val tax = federalTax.getFederalTax(0.0, provincialTax.getProvincesArray()[0] )
        Truth.assertThat(tax).isZero()
    }

    @Test
    fun getFederalTax_NegativeInput_ReturnZero() {
        val tax = federalTax.getFederalTax(-9999999.0, provincialTax.getProvincesArray()[0] )
        Truth.assertThat(tax).isZero()
    }


//    @Test
//    fun getMarginalTaxRate() {
//    }
}