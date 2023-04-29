package com.oleksiikravchuk.canadataxcalculator

class IncomeTax(private val province: Province, private val annualIncome: Double) {

    private val FederalTaxBrakets2023 = arrayOf(
        Pair(0, 0.15),
        Pair(53359, 0.205),
        Pair(106717, 0.26),
        Pair(165430, 0.29),
        Pair(235675, 0.33)
    )


    public fun getFederalTax(): Double {
        var taxValue = 0.0;

        for (i in 1..FederalTaxBrakets2023.size) {
            if (i == FederalTaxBrakets2023.size && annualIncome > FederalTaxBrakets2023[i - 1].first) {
                taxValue += (annualIncome - FederalTaxBrakets2023[i - 1].first) * FederalTaxBrakets2023[i - 1].second
                break
            }
            if (annualIncome <= FederalTaxBrakets2023[i].first) {
                taxValue += (annualIncome - FederalTaxBrakets2023[i - 1].first) * FederalTaxBrakets2023[i - 1].second
                break;
            } else {
                taxValue += (FederalTaxBrakets2023[i].first - FederalTaxBrakets2023[i - 1].first) * FederalTaxBrakets2023[i - 1].second
            }
        }
        return taxValue
    }

}
