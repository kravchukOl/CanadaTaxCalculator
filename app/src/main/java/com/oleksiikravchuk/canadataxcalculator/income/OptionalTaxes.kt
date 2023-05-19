package com.oleksiikravchuk.canadataxcalculator.income

class OptionalTaxes {

    private val  inclusionRate = 0.5

    fun getCapitalGainsTax(capitalGains : Double, marginalRate : Double ) : Double {
        if (capitalGains <= 0)
            return 0.0
        return (capitalGains * inclusionRate) * marginalRate
    }


}
