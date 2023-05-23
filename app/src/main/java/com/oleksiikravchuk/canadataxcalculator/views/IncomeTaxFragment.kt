package com.oleksiikravchuk.canadataxcalculator.views

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.oleksiikravchuk.canadataxcalculator.income.IncomeTax
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceArrayAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentIncomeTaxBinding
import com.oleksiikravchuk.canadataxcalculator.income.FederalTax
import com.oleksiikravchuk.canadataxcalculator.income.OptionalTaxes
import com.oleksiikravchuk.canadataxcalculator.income.ProvincialTax

class IncomeTaxFragment : Fragment() {

    private lateinit var binding: FragmentIncomeTaxBinding

    private var incomeTax = IncomeTax()

    private var federalTax = FederalTax()
    private var provincialTax = ProvincialTax()
    private var optionalTaxes = OptionalTaxes()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeTaxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()

        if (savedInstanceState != null)
            applySavedInstanceStates(savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Annual Income", binding.editTextAnnualIncome.text.toString())

        if (binding.tableLayoutSummary.visibility == View.VISIBLE) {
            outState.putInt("Summary Visibility", binding.cardViewSummary.visibility)
            outState.putString("Federal Tax", binding.textViewFederalTax.text.toString())
            outState.putString("Provincial Tax", binding.textViewProvincialTax.text.toString())
            outState.putString("Total Tax", binding.textViewTotalIncomeTax.text.toString())
            outState.putString(
                "EI Deduction",
                binding.textViewEmploymentInsuranceDeduction.text.toString()
            )
            outState.putString(
                "CPP Contribution",
                binding.textViewCppContribution.text.toString()
            )
            outState.putString("Net Income", binding.textViewNetIncome.text.toString())
            outState.putString("Average Tax Rate", binding.textViewAverageTaxRate.text.toString())
            outState.putString("Marginal Tax Rate", binding.textViewAverageTaxRate.text.toString())
        }
    }

    private fun applySavedInstanceStates(instanceState: Bundle) {
        binding.editTextAnnualIncome.setText(instanceState.getString("Annual Income"))

        if (instanceState.getInt("Summary Visibility") == View.VISIBLE) {
            binding.tableLayoutSummary.visibility = View.VISIBLE
            binding.textViewFederalTax.text = instanceState.getString("Federal Tax")
            binding.textViewProvincialTax.text = instanceState.getString("Provincial Tax")
            binding.textViewNetIncome.text = instanceState.getString("Total Tax")
            binding.textViewEmploymentInsuranceDeduction.text =
                instanceState.getString("EI Deduction")
            binding.textViewCppContribution.text = instanceState.getString("CPP Contribution")
            binding.textViewNetIncome.text = instanceState.getString("Net Income")
            binding.textViewAverageTaxRate.text = instanceState.getString("Average Tax Rate")
            binding.textViewMarginalTaxRate.text = instanceState.getString("Marginal Tax Rate")
        }
    }

    private fun setUI() {
        setSpinnerAdapter()
        onEnterKeyPressedInit()

        binding.textViewShowOptionsTop.setOnClickListener {
            when (binding.cardViewOptions.visibility) {
                View.GONE -> showOptions()
                else -> hideOptions()
            }
        }

//        binding.switchEiDeduction.setOnClickListener {
//
//        }

        binding.textHideOptions.setOnClickListener() {
            hideOptions()
        }

        binding.buttonCalculateTaxes.setOnClickListener {
            hideOptions()
            calculateTaxes()
        }


    }

    private fun onEnterKeyPressedInit() {
        binding.editTextAnnualIncome.setOnKeyListener(
            object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        hideOptions()
                        calculateTaxes()
                        return true
                    }
                    return false
                }
            }
        )
    }

    private fun hideOptions() {
        binding.cardViewOptions.visibility = View.GONE
    }

    private fun showOptions() {
        binding.cardViewOptions.visibility = View.VISIBLE
    }


    private fun calculateTaxes() {

        if (binding.editTextAnnualIncome.text.isNullOrEmpty()) {
            Toast.makeText(context, "Enter Annual Income", Toast.LENGTH_LONG).show()
        } else {
            binding.cardViewSummary.visibility = View.VISIBLE

            val annualIncome = binding.editTextAnnualIncome.text.toString().toDouble()
            val provinceData = binding.spinnerProvinces.selectedItem as Province
            val federal = federalTax.getFederalTax(annualIncome, provinceData)
            val provincial = provincialTax.getProvinceTax(annualIncome, provinceData)
            val employmentInsuranceDeduction = incomeTax.getEmploymentInsuranceDeduction(
                annualIncome, provinceData
            )
            val contributionCPP = incomeTax.getCanadaPensionPlanContribution(
                annualIncome, provinceData
            )

            var capitalGainsTax = 0.0

            val marginalTaxRate = federalTax.getMarginalTaxRate(annualIncome, provinceData) +
                    provincialTax.getMarginalTaxRate(annualIncome, provinceData)


            if (binding.editTextCapitalGains.text?.isNotEmpty() == true) {
                binding.tableRowCapitalGainsTax.visibility = View.VISIBLE
                val capitalGains = binding.editTextCapitalGains.text.toString().toDouble()
                capitalGainsTax = optionalTaxes.getCapitalGainsTax(capitalGains, marginalTaxRate)
                binding.textViewCapitalGainsTax.text = String.format("%.2f C$", capitalGainsTax)
            } else {
                binding.tableRowCapitalGainsTax.visibility = View.GONE
            }



            binding.textViewFederalTax.text =
                String.format("%.2f C$", federal)

            binding.textViewProvincialTax.text =
                String.format("%.2f C$", provincial)

            binding.textViewTotalIncomeTax.text =
                String.format(
                    "%.2f C$", provincial + federal + capitalGainsTax
                )


            binding.textViewEmploymentInsuranceDeduction.text =
                String.format(
                    "%.2f C$",
                    employmentInsuranceDeduction
                )


            if (provinceData.provinceName == "Quebec")
                binding.textViewCppQppContributionText.text = getText(R.string.qpp_contribution)

            binding.textViewCppContribution.text =
                String.format(
                    "%.2f C$",
                    contributionCPP
                )

            binding.textViewNetIncome.text =
                String.format(
                    "%.2f C$",
                    annualIncome - provincial - federal - contributionCPP - employmentInsuranceDeduction
                )

            binding.textViewAverageTaxRate.text =
                String.format("%.1f%%", (provincial + federal) / annualIncome * 100)

            binding.textViewMarginalTaxRate.text =
                String.format("%.1f%%", marginalTaxRate * 100)

        }
    }

    private fun setSpinnerAdapter() {
        val provincesArray = incomeTax.getIndividualsIncomeTaxRates()
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }


}