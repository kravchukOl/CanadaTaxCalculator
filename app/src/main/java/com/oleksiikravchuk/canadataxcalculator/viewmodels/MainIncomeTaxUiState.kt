package com.oleksiikravchuk.canadataxcalculator.viewmodels

class MainIncomeTaxUiState(
    val totalNetIncome: Double,
    val totalIncomeTax: Double,
    val federalTax: Double,
    val provincialTax: Double,
    val marginalTaxRate: Double,
    val averageTaxRate: Double,
) {}