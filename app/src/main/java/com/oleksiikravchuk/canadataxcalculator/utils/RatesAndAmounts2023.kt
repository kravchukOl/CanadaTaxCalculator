package com.oleksiikravchuk.canadataxcalculator.utils

import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax

object RatesAndAmounts2023 {

    // Federal Rates and Amounts:
    val federalTaxBrackets2023 = arrayOf(
        Pair(0, 0.15),
        Pair(53359, 0.205),
        Pair(106717, 0.26),
        Pair(165430, 0.2932),
        Pair(235675, 0.33)
    )
    const val personaFederalTaxCredit2023: Int = 15000
    const val federalTaxReductionForQuebec = 0.165

    // Provincial Rates and Amounts:
    val provincesAndRates2023 = arrayOf(
        Province(
            "Alberta", R.drawable.flag_of_alberta,
            arrayOf(
                Pair(0, 0.1),
                Pair(142292, 0.12),
                Pair(170751, 0.13),
                Pair(227668, 0.14),
                Pair(341502, 0.15)
            ),
            21003,
            0.0812,
            0.0218,
            arrayOf(Pair(SalesTax.SaleTaxesType.GST, 0.05))
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
            11981,
            0.12,
            0.0196,
            arrayOf(
                Pair(SalesTax.SaleTaxesType.GST, 0.05),
                Pair(SalesTax.SaleTaxesType.PST, 0.07)
            )
        ),
        Province(
            "Manitoba", R.drawable.flag_of_manitoba,
            arrayOf(
                Pair(0, 0.108),
                Pair(36842, 0.1275),
                Pair(79625, 0.174),
            ),
            15000,
            0.08,
            0.007837,
            arrayOf(
                Pair(SalesTax.SaleTaxesType.GST, 0.05),
                Pair(SalesTax.SaleTaxesType.PST, 0.07)
            )
        ),
        Province(
            "New Brunswick", R.drawable.flag_of_new_brunswick,
            arrayOf(
                Pair(0, 0.094),
                Pair(47715, 0.14),
                Pair(95431, 0.16),
                Pair(176756, 0.195),
            ),
            12458,
            0.14,
            0.0275,
            arrayOf(Pair(SalesTax.SaleTaxesType.HST, 0.15))
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
            10382,
            0.063,
            0.032,
            arrayOf(Pair(SalesTax.SaleTaxesType.HST, 0.15))
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
            8481,
            0.0885,
            0.0299,
            arrayOf(Pair(SalesTax.SaleTaxesType.HST, 0.15))
        ),
        Province(
            "Northwest Territories", R.drawable.flag_of_the_northwest_territories,
            arrayOf(
                Pair(0, 0.059),
                Pair(48326, 0.086),
                Pair(96655, 0.122),
                Pair(157139, 0.1405),
            ),
            16593,
            0.115,
            0.06,
            arrayOf(Pair(SalesTax.SaleTaxesType.GST, 0.05))
        ),
        Province(
            "Nunavut", R.drawable.flag_of_nunavut,
            arrayOf(
                Pair(0, 0.04),
                Pair(50877, 0.07),
                Pair(101754, 0.09),
                Pair(165429, 0.115)
            ),
            17925,
            0.0551,
            0.0261,
            arrayOf(Pair(SalesTax.SaleTaxesType.GST, 0.05))
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
            11865,
            0.1,
            0.02863,
            arrayOf(Pair(SalesTax.SaleTaxesType.HST, 0.13))
        ),
        Province(
            "Prince Edward Island", R.drawable.flag_of_prince_edward_island,
            arrayOf(
                Pair(0, 0.098),
                Pair(31984, 0.138),
                Pair(63969, 0.167),
            ),
            12000,
            0.105,
            0.013,
            arrayOf(Pair(SalesTax.SaleTaxesType.HST, 0.15))
        ),
        Province(
            "Quebec", R.drawable.flag_of_quebec,
            arrayOf(
                Pair(0, 0.14),
                Pair(49275, 0.19),
                Pair(98540, 0.24),
                Pair(119910, 0.2575),
            ),
            17183,
            0.117,
            0.0342,
            arrayOf(
                Pair(SalesTax.SaleTaxesType.GST, 0.05),
                Pair(SalesTax.SaleTaxesType.QST, 0.0985)
            )
        ),
        Province(
            "Saskatchewan", R.drawable.flag_of_saskatchewan,
            arrayOf(
                Pair(0, 0.105),
                Pair(49720, 0.125),
                Pair(142058, 0.145),
            ),
            17661,
            0.11,
            0.02105,
            arrayOf(
                Pair(SalesTax.SaleTaxesType.GST, 0.05),
                Pair(SalesTax.SaleTaxesType.PST, 0.06)
            )
        ),
        Province(
            "Yukon", R.drawable.flag_of_yukon, arrayOf(
                Pair(0, 0.064),
                Pair(53359, 0.09),
                Pair(106717, 0.109),
                Pair(165430, 0.128),
                Pair(500000, 0.15)
            ),
            15000,
            0.1202,
            0.0067,
            arrayOf(Pair(SalesTax.SaleTaxesType.GST, 0.05))
        ),
    )

    val ontarioSurtaxRates2023 = arrayOf(
        Pair(5315, 0.2),
        Pair(6802, 0.56)
    )
    val princeEdwardSurtaxRates2023 = arrayOf(
        Pair(12500, 0.1)
    )


    // Dividends Rates:

    const val eligibleGrossUpRate = 1.38
    const val nonEligibleGrossUpRate = 1.15

    const val federalEligibleTaxCreditRate = 0.150198
    const val federalNonEligibleTaxCreditRate = 0.90301

    // Employments Insurance Rates and Amounts:

    val federalEmploymentInsuranceRates2023 = Pair(61500, 0.0163)
    val quebecEmploymentInsuranceRates2023 = Pair(61500, 0.0127)

    // Pension Plan Rates and Amounts:

    val canadaPensionPlanRates2023 = Pair(63100, 0.0595)
    val quebecPensionPlanRates2023 = Pair(66600, 0.054)
    const val basicExemptionAmountCPP = 3500
}