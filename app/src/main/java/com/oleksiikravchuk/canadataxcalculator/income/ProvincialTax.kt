package com.oleksiikravchuk.canadataxcalculator.income

import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.models.Province

class ProvincialTax {
    private val provincesAndRates2023 = arrayOf(
        Province(
            "Alberta", R.drawable.flag_of_alberta,
            arrayOf(
                Pair(0, 0.1),
                Pair(142292, 0.12),
                Pair(170751, 0.13),
                Pair(227668, 0.14),
                Pair(341502, 0.15)
            ),
            21003
        ),
        Province(
            "British Columbia", R.drawable.flag_of_british_columbia,
            arrayOf(
                Pair(0, 0.0506),
                Pair(45654, 0.077),
                Pair(91310, 0.105),
                Pair(104835, 0.1229),
                Pair(127299, 0.147),
                Pair(172602, 0.168),
                Pair(240716, 0.205),
            ),
            11981
        ),
        Province(
            "Manitoba", R.drawable.flag_of_manitoba,
            arrayOf(
                Pair(0, 0.108),
                Pair(36842, 0.1275),
                Pair(79625, 0.174),
            ),
            15000
        ),
        Province(
            "New Brunswick", R.drawable.flag_of_new_brunswick,
            arrayOf(
                Pair(0, 0.094),
                Pair(47715, 0.14),
                Pair(95431, 0.16),
                Pair(176756, 0.195),
            ),
            12458
        ),
        Province(
            "Newfoundland and Labrador", R.drawable.flag_of_newfoundland_and_labrador,
            arrayOf(
                Pair(0, 0.087),
                Pair(41457, 0.145),
                Pair(82913, 0.158),
                Pair(148027, 0.178),
                Pair(207239, 0.198),
                Pair(264750, 0.208),
                Pair(529500, 0.213),
                Pair(1059000, 0.218),
            ),
            10382
        ),
        Province(
            "Nova Scotia", R.drawable.flag_of_nova_scotia,
            arrayOf(
                Pair(0, 0.0879),
                Pair(29590, 0.1495),
                Pair(59180, 0.1667),
                Pair(93000, 0.175),
                Pair(150000, 0.21)
            ),
            8481
        ),
        Province(
            "Northwest Territories", R.drawable.flag_of_the_northwest_territories,
            arrayOf(
                Pair(0, 0.059),
                Pair(48326, 0.086),
                Pair(96655, 0.122),
                Pair(157139, 0.1405),
            ),
            16593
        ),
        Province(
            "Nunavut", R.drawable.flag_of_nunavut,
            arrayOf(
                Pair(0, 0.04),
                Pair(50877, 0.07),
                Pair(101754, 0.09),
                Pair(165429, 0.115)
            ),
            17925
        ),
        Province(
            "Ontario", R.drawable.flag_of_ontario,
            arrayOf(
                Pair(0, 0.0505),
                Pair(49231, 0.0915),
                Pair(98463, 0.1116),
                Pair(150000, 0.1216),
                Pair(220000, 0.1316)
            ),
            11865
        ),
        Province(
            "Prince Edward Island", R.drawable.flag_of_prince_edward_island,
            arrayOf(
                Pair(0, 0.098),
                Pair(31984, 0.138),
                Pair(63969, 0.167),
            ),
            12000
        ),
        Province(
            // Quebec income tax rates
            "Quebec", R.drawable.flag_of_quebec, arrayOf(
                Pair(0, 0.14),
                Pair(49275, 0.19),
                Pair(98540, 0.24),
                Pair(119910, 0.2575),
            ),
            17183
        ),
        Province(
            "Saskatchewan", R.drawable.flag_of_saskatchewan,
            arrayOf(
                Pair(0, 0.105),
                Pair(49720, 0.125),
                Pair(142058, 0.145),
            ),
            17661
        ),
        Province(
            "Yukon", R.drawable.flag_of_yukon, arrayOf(
                Pair(0, 0.064),
                Pair(53359, 0.09),
                Pair(106717, 0.109),
                Pair(165430, 0.128),
                Pair(500000, 0.15)
            ),
            15000
        ),
    )
    private val ontarioSurtaxRates = arrayOf(
        Pair(5315, 0.2),
        Pair(6802, 0.56)
    )

    fun getProvincesArray() = provincesAndRates2023

    fun getProvinceTax(annualIncome: Double, province: Province): Double {
        return if (province.provinceName == "Ontario") {
            val baseProvinceTax = calculateTaxCommonRates(
                annualIncome,
                province.provinceTaxRates,
                province.provinceTaxCredit
            )
            baseProvinceTax + getSurtaxForOntario(baseProvinceTax)
        } else {
            calculateTaxCommonRates(
                annualIncome,
                province.provinceTaxRates,
                province.provinceTaxCredit
            )
        }
    }

    private fun getSurtaxForOntario(baseOntarioProvinceTax: Double): Double {
        if (baseOntarioProvinceTax <= ontarioSurtaxRates[0].first) {
            return 0.0
        }
        return if (baseOntarioProvinceTax <= ontarioSurtaxRates[1].first) {
            (baseOntarioProvinceTax - ontarioSurtaxRates[0].first) * ontarioSurtaxRates[0].second
        } else {
            val firstTier =
                (ontarioSurtaxRates[1].first - ontarioSurtaxRates[0].first) * ontarioSurtaxRates[0].second
            val secondTier =
                (baseOntarioProvinceTax - ontarioSurtaxRates[1].first) * ontarioSurtaxRates[1].second
            firstTier + secondTier
        }
    }

    fun getMarginalTaxRate(annualIncome: Double, province: Province) : Double{
        if (annualIncome <= 0)
            return 0.0
        var marginalRate = 0.0;

        for (i in 1 until province.provinceTaxRates.size) {
            if (annualIncome < province.provinceTaxRates[i].first)
                marginalRate = province.provinceTaxRates[i - 1].second
        }
        return marginalRate
    }

}