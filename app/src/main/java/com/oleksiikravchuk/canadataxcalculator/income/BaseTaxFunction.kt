package com.oleksiikravchuk.canadataxcalculator.income

fun calculateTaxCommonRates(
    annualIncome: Double,
    ratesArray: Array<Pair<Int, Double>>,
    taxCredit: Int
): Double {
    var taxValue = 0.0
    if (annualIncome <= taxCredit) return 0.0

    for (i in 1..ratesArray.size) {
        if (i == ratesArray.size && annualIncome > ratesArray[i - 1].first) {
            taxValue += (annualIncome - ratesArray[i - 1].first) * ratesArray[i - 1].second
            break
        }
        if (annualIncome <= ratesArray[i].first) {
            taxValue += (annualIncome - ratesArray[i - 1].first) * ratesArray[i - 1].second
            break
        } else {
            taxValue += (ratesArray[i].first - ratesArray[i - 1].first) * ratesArray[i - 1].second
        }
    }
    return taxValue - taxCredit * ratesArray[0].second
}