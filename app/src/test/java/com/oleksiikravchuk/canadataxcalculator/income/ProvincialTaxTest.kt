package com.oleksiikravchuk.canadataxcalculator.income

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ProvincialTaxTest {

    lateinit var provincialTax: ProvincialTax

    @Before
    fun setup() {
        provincialTax = ProvincialTax()
    }

    @Test
    fun getSurtaxForOntario_provinceTax_6000_result_137_() {
        val surTax = provincialTax.getSurtaxForOntario(6000.0)
        assertThat(surTax).isWithin(1.0e-10).of(137.0)
    }

    @Test
    fun getSurtaxForOntario_provinceTax_6802_result_297_4() {
        val surTax = provincialTax.getSurtaxForOntario(6802.0)
        assertThat(surTax).isWithin(1.0e-10).of(297.4)
    }

    @Test
    fun getSurtaxForOntario_provinceTax_10000_result_2088_28() {
        val surTax = provincialTax.getSurtaxForOntario(10000.0)
        assertThat(surTax).isWithin(1.0e-10).of(2088.28)
    }


    @Test
    fun getSurtax_provinceTax_12500_surtaxRatesForPrinceEdward_result_0() {
        val surTax = provincialTax.getSurtax(
            12500.0, arrayOf(
                Pair(12500, 0.1)
            )
        )
        assertThat(surTax).isZero()
    }

    @Test
    fun getSurtax_provinceTax_24500_surtaxRatesForPrinceEdward_result_1200() {
        val surTax = provincialTax.getSurtax(
            24500.0, arrayOf(
                Pair(12500, 0.1)
            )
        )
        assertThat(surTax).isWithin(1.0e-10).of(1200.0)
    }

    @Test
    fun getSurtax_provinceTax_50000_surtaxRatesForPrinceEdward_result_3750() {
        val surTax = provincialTax.getSurtax(
            50000.0, arrayOf(
                Pair(12500, 0.1)
            )
        )
        assertThat(surTax).isWithin(1.0e-10).of(3750.0)
    }

    @Test
    fun getSurtax_provinceTax_5315_surtaxRatesForOntario_result_0() {
        val surTax = provincialTax.getSurtax(
            5315.0, arrayOf(
                Pair(5315, 0.2),
                Pair(6802, 0.56)
            )
        )
        assertThat(surTax).isZero()
    }

    @Test
    fun getSurtax_provinceTax_6000_surtaxRatesForOntario_result_137_0() {
        val surTax = provincialTax.getSurtax(
            6000.0, arrayOf(
                Pair(5315, 0.2),
                Pair(6802, 0.56)
            )
        )
        assertThat(surTax).isWithin(1.0e-10).of(137.0)
    }

    @Test
    fun getSurtax_provinceTax_6802_surtaxRatesForOntario_result_297_4() {
        val surTax = provincialTax.getSurtax(
            6802.0, arrayOf(
                Pair(5315, 0.2),
                Pair(6802, 0.56)
            )
        )
        assertThat(surTax).isWithin(1.0e-10).of(297.4)
    }

    @Test
    fun getSurtax_provinceTax_10000_surtaxRatesForOntario_result_2088_28() {
        val surTax = provincialTax.getSurtax(
            10000.0, arrayOf(
                Pair(5315, 0.2),
                Pair(6802, 0.56)
            )
        )
        assertThat(surTax).isWithin(1.0e-10).of(2088.28)
    }


}