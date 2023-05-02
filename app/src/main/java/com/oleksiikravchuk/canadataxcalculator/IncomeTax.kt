package com.oleksiikravchuk.canadataxcalculator

class IncomeTax() {

    private val federalTaxBrackets2023 = arrayOf(
        Pair(0, 0.15),
        Pair(53359, 0.205),
        Pair(106717, 0.26),
        Pair(165430, 0.29),
        Pair(235675, 0.33)
    )

    private val individualsIncomeTaxRates2023 = arrayOf(
        Province(
            "Alberta", R.drawable.flag_of_alberta,
            arrayOf(
                Pair(0, 0.1),
                Pair(142058, 0.12),
                Pair(170751, 0.13),
                Pair(227668, 0.14),
                Pair(341502, 0.15)
            )
        ),
        Province(
            "British Columbia", R.drawable.flag_of_british_columbia,
            arrayOf(
                Pair(0, 0.0504),
                Pair(45654, 0.077),
                Pair(91310, 0.105),
                Pair(104835, 0.1229),
                Pair(172602, 0.147),
                Pair(240716, 0.168),
                Pair(341502, 0.205),
            )
        ),
        Province(
            "Manitoba", R.drawable.flag_of_manitoba,
            arrayOf(
                Pair(0, 0.108),
                Pair(36842, 0.1275),
                Pair(79625, 0.174),
            )
        ),
        Province(
            "New Brunswick", R.drawable.flag_of_new_brunswick,
            arrayOf(
                Pair(0, 0.094),
                Pair(47715, 0.14),
                Pair(95431, 0.16),
                Pair(176756, 0.195),
            )
        ),
        Province(
            "Newfoundland and Labrador", R.drawable.flag_of_newfoundland_and_labrador,
            arrayOf(
                Pair(0, 0.087),
                Pair(41457, 0.145),
                Pair(82913, 0.158),
                Pair(148027, 0.158),
                Pair(207239, 0.178),
                Pair(264750, 0.198),
                Pair(529500, 0.208),
                Pair(1059000, 0.213),
            )
        ),
        Province(
            "Northwest Territories", R.drawable.flag_of_the_northwest_territories,
            arrayOf(
                Pair(0, 0.087),
                Pair(41457, 0.145),
                Pair(82913, 0.158),
                Pair(148027, 0.158),
                Pair(207239, 0.178),
                Pair(264750, 0.198),
                Pair(529500, 0.208),
                Pair(1059000, 0.213),
            )
        ),
        Province(
            "Nova Scotia", R.drawable.flag_of_nova_scotia,
            arrayOf(
                Pair(0, 0.0879),
                Pair(29590, 0.1495),
                Pair(59180, 0.1667),
                Pair(93000, 0.175),
                Pair(150000, 0.21)
            )
        ),
        Province(
            "Nunavut", R.drawable.flag_of_nunavut,
            arrayOf(
                Pair(0, 0.04),
                Pair(50877, 0.07),
                Pair(101754, 0.09),
                Pair(165429, 0.115)
            )
        ),
        Province(
            "Ontario", R.drawable.flag_of_ontario,
            arrayOf(
                Pair(0, 0.0505),
                Pair(49231, 0.0915),
                Pair(98463, 0.1116),
                Pair(150000, 0.1216),
                Pair(220000, 0.1316)
            )
        ),
        Province(
            "Prince Edward Island", R.drawable.flag_of_prince_edward_island,
            arrayOf(
                Pair(0, 0.098),
                Pair(31984, 0.138),
                Pair(63969, 0.167),
            )
        ),
        Province(
            // Quebec income tax rates
            "Quebec", R.drawable.flag_of_quebec, arrayOf(
                Pair(0, 0.14),
                Pair(49275, 0.19),
                Pair(98540, 0.24),
                Pair(119910, 0.2575),
            )
        ),
        Province(
            "Saskatchewan", R.drawable.flag_of_saskatchewan,
            arrayOf(
                Pair(0, 0.105),
                Pair(49720, 0.125),
                Pair(142058, 0.145),
            )
        ),
        Province(
            "Yukon", R.drawable.flag_of_yukon, arrayOf(
                Pair(0, 0.064),
                Pair(53359, 0.09),
                Pair(106717, 0.109),
                Pair(165430, 0.128),
                Pair(500000, 0.15)
            )
        ),
    )


    fun getIndividualsIncomeTaxRates() = individualsIncomeTaxRates2023

    fun getFederalTax(annualIncome: Double) = calculateTaxCommonRates(annualIncome)

    fun getProvinceTax(annualIncome: Double, province: Province) =
        calculateTaxCommonRates(annualIncome, province)


    private fun calculateTaxCommonRates(annualIncome: Double, province: Province? = null): Double {
        var taxValue = 0.0
        val ratesArray = province?.individualsIncomeTaxRates ?: federalTaxBrackets2023

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
        return taxValue
    }
}
