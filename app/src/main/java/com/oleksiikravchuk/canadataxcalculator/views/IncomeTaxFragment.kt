package com.oleksiikravchuk.canadataxcalculator.views

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceArrayAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentIncomeTaxBinding
import com.oleksiikravchuk.canadataxcalculator.income.*

class IncomeTaxFragment : Fragment() {

    private lateinit var binding: FragmentIncomeTaxBinding

    //private var incomeTax = IncomeTax()

    private val federalTax = FederalTax()
    private val provincialTax = ProvincialTax()
    private val deductions = Deductions()
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

        setSpinnerAdapter()
        initListeners()

        binding.buttonCalculateTaxes.visibility = View.GONE

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

    private fun initListeners() {
        onEnterKeyPressedInit()

        binding.editTextAnnualIncome.addTextChangedListener {
            calculateTaxes()
        }

        binding.spinnerProvinces.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!binding.editTextAnnualIncome.text.isNullOrEmpty())
                        calculateTaxes()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }


        binding.textViewShowOptionsTop.setOnClickListener {
            when (binding.cardViewOptions.visibility) {
                View.GONE -> showOptions()
                else -> hideOptions()
            }
        }

        binding.switchEiDeduction.setOnClickListener {
            it as SwitchMaterial

            if (it.isChecked) {
                binding.tableRowEmploymentInsuranceDeduction.visibility = View.VISIBLE
            } else {
                binding.tableRowEmploymentInsuranceDeduction.visibility = View.GONE
            }
            calculateTaxes()
        }

        binding.switchCppDeduction.setOnClickListener {
            it as SwitchMaterial

            if (it.isChecked) {
                binding.tableRowCppQppContribution.visibility = View.VISIBLE
            } else {
                binding.tableRowCppQppContribution.visibility = View.GONE
            }
            calculateTaxes()
        }

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

    private fun getTotalTaxableIncome(
        basicIncome: Double,
        contributionRRCP: Double = 0.0,
        capitalGains: Double = 0.0,
        eligibleDividends: Double = 0.0,
        ineligibleDividends : Double = 0.0
    ): Double {

        //al taxableIncome = basicIncome + (capitalGains * 0.5);


        return basicIncome + (capitalGains * 0.5) - contributionRRCP
  0  }

    private fun calculateTaxes() {

        if (binding.editTextAnnualIncome.text.isNullOrEmpty()) {
            Toast.makeText(context, "Enter Annual Income", Toast.LENGTH_LONG).show()
        } else {
            binding.cardViewSummary.visibility = View.VISIBLE

            val annualIncome = binding.editTextAnnualIncome.text.toString().toDouble()

            var capitalGains = 0.0

            val provinceData = binding.spinnerProvinces.selectedItem as Province
            val federal = federalTax.getFederalTax(annualIncome, provinceData)
            val provincial = provincialTax.getProvinceTax(annualIncome, provinceData)


            var contributionsRRSP = 0.0
            var capitalGainsTax = 0.0
            var employmentInsuranceDeduction = 0.0
            var contributionCPP = 0.0


            if (binding.switchEiDeduction.isChecked) {
                employmentInsuranceDeduction = deductions.getEmploymentInsuranceDeduction(
                    annualIncome, provinceData
                )
            }

            if (binding.switchCppDeduction.isChecked) {
                contributionCPP = deductions.getCanadaPensionPlanContribution(
                    annualIncome, provinceData, binding.switchSelfEmployed.isChecked
                )
            }


            val marginalTaxRate = federalTax.getMarginalTaxRate(annualIncome, provinceData) +
                    provincialTax.getMarginalTaxRate(annualIncome, provinceData)


            if (binding.editTextCapitalGains.text?.isNotEmpty() == true) {
                binding.tableRowCapitalGainsTax.visibility = View.VISIBLE
                capitalGains = binding.editTextCapitalGains.text.toString().toDouble()
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
                    annualIncome - provincial - federal - contributionCPP - employmentInsuranceDeduction + capitalGains - capitalGainsTax
                )

            binding.textViewAverageTaxRate.text =
                String.format("%.1f%%", (provincial + federal) / annualIncome * 100)

            binding.textViewMarginalTaxRate.text =
                String.format("%.1f%%", marginalTaxRate * 100)

        }
    }

    private fun setSpinnerAdapter() {
        val provincesArray = provincialTax.provincesAndRates2023
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }


}