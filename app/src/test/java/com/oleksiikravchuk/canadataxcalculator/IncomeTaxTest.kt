package com.oleksiikravchuk.canadataxcalculator

import com.google.common.truth.Truth.assertThat
import com.oleksiikravchuk.canadataxcalculator.income.IncomeTax
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class IncomeTaxTest {

    lateinit var incomeTax: IncomeTax


    @Before
    fun setup() {
        incomeTax = IncomeTax()
    }

    @After
    fun teardown() {

    }


    @Test
    fun federalTaxTest_InputZero_ReturnZero() {
        val tax = incomeTax.getFederalTax(0.0, incomeTax.getProvincesArray()[0])
        assertThat(tax).isZero()
    }

    @Test
    fun federalTaxTest_InputNegative_ReturnZero() {
        val tax = incomeTax.getFederalTax(-999999.0, incomeTax.getProvincesArray()[0])
        assertThat(tax).isZero()
    }


    @Test
    fun federalTaxTest_Input15000_Return015() {
        val tax = incomeTax.getFederalTax(15001.0, incomeTax.getProvincesArray()[0])
        assertThat(tax).isAtLeast(0.15)
        assertThat(tax).isAtMost(0.150001)

    }

    @Test
    fun federalTaxTest_Input53359_Return8003_85() {
        val tax = incomeTax.getFederalTax(53359.0, incomeTax.getProvincesArray()[0])
        assertThat(tax).isAtLeast(5753.84)
        assertThat(tax).isAtMost(5753.86)

    }


    @Test
    fun totalTaxesTestInputNegative() {
        for (province in incomeTax.getProvincesArray()) {
            val totalTax =
                incomeTax.getProvinceTax(-10000.0, province) + incomeTax.getFederalTax(
                    -10000.0,
                    incomeTax.getProvincesArray()[0]
                )
            assertThat(totalTax).isZero()
        }
    }

    @Test
    fun totalTaxesTestInputZero() {
        for (province in incomeTax.getProvincesArray()) {
            val totalTax = incomeTax.getProvinceTax(0.0, province) + incomeTax.getFederalTax(
                0.0,
                incomeTax.getProvincesArray()[0]
            )
            assertThat(totalTax).isZero()
        }
    }

    @Test
    fun totalTaxesTest_Input10000_ReturnZero() {
        for (province in incomeTax.getProvincesArray()) {
            val totalTax =
                incomeTax.getProvinceTax(10000.0, province) + incomeTax.getFederalTax(
                    10000.0,
                    incomeTax.getProvincesArray()[0]
                )
            assertThat(totalTax).isZero()
        }
    }

}