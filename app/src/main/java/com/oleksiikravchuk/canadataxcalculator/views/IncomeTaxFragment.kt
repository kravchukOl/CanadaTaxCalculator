package com.oleksiikravchuk.canadataxcalculator.views

import android.os.Bundle
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceArrayAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentIncomeTaxBinding
import com.oleksiikravchuk.canadataxcalculator.income.*
import com.oleksiikravchuk.canadataxcalculator.utils.RatesAndAmounts2023
import com.oleksiikravchuk.canadataxcalculator.utils.RatesAndAmounts2023.provincesAndRates2023
import com.oleksiikravchuk.canadataxcalculator.viewmodels.IncomeTaxViewModel

class IncomeTaxFragment : Fragment() {

    private lateinit var binding: FragmentIncomeTaxBinding
    private lateinit var viewModel: IncomeTaxViewModel

    private val provincialTax = ProvincialTax()

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

        viewModel = ViewModelProvider(this)[IncomeTaxViewModel::class.java]

        setSpinnerAdapter()
        restoreInput()
        initObservers()
        initListeners()

        binding.buttonCalculateTaxes.visibility = View.GONE
    }

    private fun restoreInput() {
        val spinnerPosition =
            provincialTax.provincesAndRates2023.indexOf(viewModel.selectedProvince)
        binding.spinnerProvinces.setSelection(spinnerPosition)
        if (viewModel.basicIncome > 0)
            binding.editTextAnnualIncome.setText(viewModel.basicIncome.toString())
        if (viewModel.contributionRRCP > 0)
            binding.editTextRrcp.setText(viewModel.contributionRRCP.toString())
        if (viewModel.capitalGains > 0)
            binding.editTextCapitalGains.setText(viewModel.capitalGains.toString())
        if (viewModel.eligibleDividends > 0)
            binding.editTextEligibleDividends.setText(viewModel.eligibleDividends.toString())
        if (viewModel.nonEligibleDividends > 0)
            binding.textInputLayoutNonEligibleDividends.setText(viewModel.nonEligibleDividends.toString())

        binding.switchEiDeduction.isChecked = viewModel.isEiIncluded
        binding.switchCppDeduction.isChecked = viewModel.isCppIncluded
        binding.switchSelfEmployed.isChecked = viewModel.isSelfEmployed

        if (viewModel.containsData)
            viewModel.calculate()
    }


    private fun initObservers() {
        val totalTaxableIncome: LiveData<Double> = viewModel.totalTaxableIncome
        totalTaxableIncome.observe(viewLifecycleOwner) { income ->
            binding.textViewTotalTaxableIncome.text = String.format("%.2f C$", income)
            if (totalTaxableIncome.value != binding.editTextAnnualIncome.text.toString().toDouble())
                binding.tableRowTotalTaxableIncome.visibility = View.VISIBLE
        }

        val federalTax: LiveData<Double> = viewModel.federalTax
        federalTax.observe(viewLifecycleOwner) { tax ->
            binding.textViewFederalTax.text = String.format("%.2f C$", tax)
        }

        val provincialTax: LiveData<Double> = viewModel.provincialTax
        provincialTax.observe(viewLifecycleOwner) { tax ->
            binding.textViewProvincialTax.text = String.format("%.2f C$", tax)
        }

        val surtax: LiveData<Double> = viewModel.provinceSurtax
        surtax.observe(viewLifecycleOwner) { tax ->
            binding.textViewSurtaxText.text = String.format(
                getString(R.string.province_surtax),
                viewModel.selectedProvince.provinceName
            )
            binding.tableRowSurtax.visibility = View.VISIBLE
            binding.textViewSurtax.text = String.format("%.2f C$", tax)
        }

        val capitalGainsTax: LiveData<Double> = viewModel.capitalGainsTax
        capitalGainsTax.observe(viewLifecycleOwner) { tax ->
            binding.tableRowCapitalGainsTax.visibility = View.VISIBLE
            binding.textViewCapitalGainsTax.text = String.format("%.2f C$", tax)
        }

        val totalIncomeTax: LiveData<Double> = viewModel.totalIncomeTax
        totalIncomeTax.observe(viewLifecycleOwner) { tax ->
            binding.textViewTotalIncomeTax.text = String.format("%.2f C$", tax)
        }

        val deductionEI: LiveData<Double> = viewModel.deductionEI
        deductionEI.observe(viewLifecycleOwner) { deduction ->
            binding.tableRowEmploymentInsuranceDeduction.visibility = View.VISIBLE
            binding.textViewEmploymentInsuranceDeduction.text = String.format("%.2f C$", deduction)
        }

        val contributionCPP: LiveData<Double> = viewModel.contributionCPP
        contributionCPP.observe(viewLifecycleOwner) { contribution ->
            binding.tableRowCppQppContribution.visibility = View.VISIBLE
            binding.textViewCppContribution.text =
                String.format("%.2f C$", contribution)
        }

        val totalNetIncome: LiveData<Double> = viewModel.totalNetIncome
        totalNetIncome.observe(viewLifecycleOwner) { income ->
            binding.textViewNetIncome.text = String.format("%.2f C$", income)
        }

        val averageTaxRate: LiveData<Double> = viewModel.averageTaxRate
        averageTaxRate.observe(viewLifecycleOwner) { rate ->
            binding.textViewAverageTaxRate.text = String.format("%.1f%%", rate)
        }

        val marginalTaxRate: LiveData<Double> = viewModel.marginalTaxRate
        marginalTaxRate.observe(viewLifecycleOwner) { rate ->
            binding.textViewMarginalTaxRate.text = String.format("%.1f%%", rate)
        }
    }

    private fun initListeners() {
        onEnterKeyPressedInit()

        binding.editTextAnnualIncome.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.basicIncome = 0.0
            } else {
                viewModel.basicIncome = binding.editTextAnnualIncome.text.toString().toDouble()
            }
            calculateTaxes()
        }

        binding.editTextRrcp.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.contributionRRCP = 0.0
            } else {
                viewModel.contributionRRCP = binding.editTextRrcp.text.toString().toDouble()
            }
            calculateTaxes()
        }

        binding.editTextCapitalGains.addTextChangedListener {
            if( it.isNullOrEmpty()) {
                viewModel.capitalGains = 0.0
            } else {
                viewModel.capitalGains = binding.editTextCapitalGains.text.toString().toDouble()
            }
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
                    if (!binding.editTextAnnualIncome.text.isNullOrEmpty()) {
                        viewModel.selectedProvince =
                            binding.spinnerProvinces.selectedItem as Province
                        calculateTaxes()
                    }
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
            viewModel.isEiIncluded = it.isChecked
            calculateTaxes()
        }

        binding.switchCppDeduction.setOnClickListener {
            it as SwitchMaterial
            viewModel.isCppIncluded = it.isChecked
            calculateTaxes()
        }

        binding.textHideOptions.setOnClickListener {
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
                        //calculateTaxes()
                        viewModel.calculate()
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
            Toast.makeText(context, getString(R.string.enter_annual_income), Toast.LENGTH_LONG)
                .show()
        } else {
            binding.cardViewSummary.visibility = View.VISIBLE
            disableOptionalRows()
            viewModel.calculate()
        }
    }

    private fun disableOptionalRows() {
        binding.tableRowCppQppContribution.visibility = View.GONE
        binding.tableRowCapitalGainsTax.visibility = View.GONE
        binding.tableRowSurtax.visibility = View.GONE
        binding.tableRowEmploymentInsuranceDeduction.visibility = View.GONE
        binding.tableRowTotalTaxableIncome.visibility = View.GONE
    }

    private fun setSpinnerAdapter() {
        val provincesArray = provincialTax.provincesAndRates2023
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }


}