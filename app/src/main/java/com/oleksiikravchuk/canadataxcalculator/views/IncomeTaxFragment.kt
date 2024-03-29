package com.oleksiikravchuk.canadataxcalculator.views

import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentIncomeTaxBinding
import com.oleksiikravchuk.canadataxcalculator.utils.RatesAndAmounts2023
import com.oleksiikravchuk.canadataxcalculator.viewmodels.IncomeTaxViewModel

class IncomeTaxFragment : Fragment() {

    private lateinit var binding: FragmentIncomeTaxBinding
    private val viewModel: IncomeTaxViewModel by viewModels()

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

        restoreInput()
        setSpinnerAdapter()
        initObservers()
        initListeners()
    }

    private fun restoreInput() {
        if (viewModel.basicIncome > 0)
            binding.editTextAnnualIncome.setText(viewModel.basicIncome.toString())
        if (viewModel.contributionRRSP > 0)
            binding.editTextRrcp.setText(viewModel.contributionRRSP.toString())
        if (viewModel.capitalGains > 0)
            binding.editTextCapitalGains.setText(viewModel.capitalGains.toString())
        if (viewModel.eligibleDividends > 0)
            binding.editTextEligibleDividends.setText(viewModel.eligibleDividends.toString())
        if (viewModel.nonEligibleDividends > 0)
            binding.editTextNonEligibleDividends.setText(viewModel.nonEligibleDividends.toString())

        binding.switchEiDeduction.isChecked = viewModel.isEiIncluded
        binding.switchCppDeduction.isChecked = viewModel.isCppIncluded
        binding.switchSelfEmployed.isChecked = viewModel.isSelfEmployed

        if (viewModel.containsData)
            viewModel.calculate()
    }

    private fun initObservers() {

        viewModel.mainIncomeTaxUiStates.observe(viewLifecycleOwner) { uiState ->
            binding.textViewFederalTax.text = String.format("%.2f$", uiState.federalTax)
            binding.textViewProvincialTax.text = String.format("%.2f$", uiState.provincialTax)
            binding.textViewTotalIncomeTax.text = String.format("%.2f$", uiState.totalIncomeTax)
            binding.textViewNetIncome.text = String.format("%.2f$", uiState.totalNetIncome)
            binding.textViewAverageTaxRate.text = String.format("%.1f%%", uiState.averageTaxRate)
            binding.textViewMarginalTaxRate.text = String.format("%.1f%%", uiState.marginalTaxRate)
        }

        viewModel.totalActualIncome.observe(viewLifecycleOwner) { income ->
            binding.textViewTotalActualIncome.text = String.format("%.2f$", income)
            binding.tableRowTotalActualIncome.visibility = View.VISIBLE
        }

        viewModel.totalTaxableIncome.observe(viewLifecycleOwner) { income ->
            binding.textViewTotalTaxableIncome.text = String.format("%.2f$", income)
            if (income != binding.editTextAnnualIncome.text.toString().toDouble())
                binding.tableRowTotalTaxableIncome.visibility = View.VISIBLE
        }

        viewModel.provinceSurtax.observe(viewLifecycleOwner) { tax ->
            binding.tableRowSurtax.visibility = View.VISIBLE
            binding.textViewSurtax.text = String.format("%.2f$", tax)
        }

        viewModel.dividendTaxCredit.observe((viewLifecycleOwner)) { credit ->
            binding.textViewDividendCredit.text = String.format("%.2f$", credit)
            binding.tableRowDividendCredit.visibility = View.VISIBLE
        }

        viewModel.deductionEI.observe(viewLifecycleOwner) { deduction ->
            binding.tableRowEmploymentInsuranceDeduction.visibility = View.VISIBLE
            binding.textViewEmploymentInsuranceDeduction.text = String.format("%.2f$", deduction)
        }

        viewModel.contributionCPP.observe(viewLifecycleOwner) { contribution ->
            binding.tableRowCppQppContribution.visibility = View.VISIBLE
            binding.textViewCppContribution.text =
                String.format("%.2f$", contribution)
        }

        viewModel.rrspRefund.observe(viewLifecycleOwner) { refund ->
            binding.tableRowRrspRefund.visibility = View.VISIBLE
            binding.textViewRrspRefund.text = String.format("%.2f$", refund)
        }
    }

    private fun initListeners() {
        onEnterKeyPressedInit()


        binding.editTextAnnualIncome.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.basicIncome = 0.0
            } else {
                viewModel.basicIncome = it.toString().toDouble()
            }
            calculateTaxes()
        }


        binding.editTextRrcp.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.contributionRRSP = 0.0
            } else {
                viewModel.contributionRRSP = it.toString().toDouble()
            }
            calculateTaxes()
        }

        binding.editTextCapitalGains.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.capitalGains = 0.0
            } else {
                viewModel.capitalGains = it.toString().toDouble()
            }
            calculateTaxes()
        }

        binding.editTextEligibleDividends.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.eligibleDividends = 0.0
            } else {
                viewModel.eligibleDividends = it.toString().toDouble()
            }
            calculateTaxes()
        }

        binding.editTextNonEligibleDividends.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                viewModel.nonEligibleDividends = 0.0
            } else {
                viewModel.nonEligibleDividends = it.toString().toDouble()
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
                    viewModel.selectedProvince =
                        binding.spinnerProvinces.selectedItem as Province
                    if (!binding.editTextAnnualIncome.text.isNullOrEmpty()) {
                        if (viewModel.containsData) {
                            calculateTaxes()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.buttonShowOptionsTop.setOnClickListener {
            when (binding.constraintLayoutOptions.visibility) {
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

        binding.switchSelfEmployed.setOnClickListener {
            it as SwitchMaterial
            viewModel.isSelfEmployed = it.isChecked
            calculateTaxes()
        }

        binding.buttonHideOptions.setOnClickListener {
            hideOptions()
        }

        binding.buttonRrspContributionInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.RRSP)
        }

//        binding.buttonRrspContribution.setOnFocusChangeListener { v, hasFocus ->
//            if(hasFocus) {
//                (v as ImageButton).setImageResource()
//            }
//        }

        binding.buttonCapitalGainsInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.CapitalGains)
        }
        binding.buttonEligibleDividendsInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.EligibleDividends)
        }
        binding.buttonNonEligibleDividendsInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.NonEligibleDividends)
        }
        binding.textViewEmploymentInsuranceInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.EiDeduction)
        }
        binding.textViewCppInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.CppContribution)
        }
        binding.textViewSelfEmployedInfo.setOnClickListener {
            closeSoftKeyboard()
            showEntryInfoSnackBar(EntryInfo.SelfEmployed)
        }
    }

    private fun onEnterKeyPressedInit() {
        binding.editTextAnnualIncome.setOnKeyListener(
            object : View.OnKeyListener {
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        hideOptions()
                        viewModel.calculate()
                        return true
                    }
                    return false
                }
            }
        )
    }

    private fun hideOptions() {
        TransitionManager.beginDelayedTransition(binding.cardViewOptions, AutoTransition())
        binding.constraintLayoutOptions.visibility = View.GONE
    }

    private fun showOptions() {
        TransitionManager.beginDelayedTransition(binding.cardViewOptions, AutoTransition())
        binding.constraintLayoutOptions.visibility = View.VISIBLE
    }

    private fun calculateTaxes() {
        if (binding.editTextAnnualIncome.text.isNullOrEmpty()) {
            binding.editTextAnnualIncome.setText("0")
            viewModel.basicIncome = 0.0
        }
        binding.cardViewSummary.visibility = View.VISIBLE
        disableOptionalRows()
        viewModel.calculate()
    }

    private fun disableOptionalRows() {
        binding.tableRowCppQppContribution.visibility = View.GONE
//        binding.tableRowCapitalGainsTax.visibility = View.GONE
        binding.tableRowSurtax.visibility = View.GONE
        binding.tableRowEmploymentInsuranceDeduction.visibility = View.GONE
        binding.tableRowTotalTaxableIncome.visibility = View.GONE
//        binding.tableRowEligibleTax.visibility = View.GONE
//        binding.tableRowNonEligibleTax.visibility = View.GONE
        binding.tableRowRrspRefund.visibility = View.GONE
        binding.tableRowTotalActualIncome.visibility = View.GONE
    }

    private fun setSpinnerAdapter() {
        val provincesArray = RatesAndAmounts2023.provincesAndRates2023
        binding.spinnerProvinces.adapter = ProvinceAdapter(provincesArray)
    }

    private fun showEntryInfoSnackBar(entryInfo: EntryInfo) {
        when (entryInfo) {
            EntryInfo.RRSP -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.rrsp_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()

            EntryInfo.CapitalGains -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.capital_gains_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()

            EntryInfo.EligibleDividends -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.eligible_dividends_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()

            EntryInfo.NonEligibleDividends -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.non_eligible_dividends_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()

            EntryInfo.EiDeduction -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.ei_deduction_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()

            EntryInfo.CppContribution -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.cpp_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()

            EntryInfo.SelfEmployed -> Snackbar.make(
                binding.linearLayoutIncomeTax,
                getText(R.string.self_employed_info),
                Snackbar.LENGTH_INDEFINITE
            ).setTextMaxLines(10).setAction("Dismiss") {}.show()
        }
    }

    private fun closeSoftKeyboard() {
        val focusView = activity?.currentFocus
        focusView?.let { view ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    enum class EntryInfo {
        RRSP,
        CapitalGains,
        EligibleDividends,
        NonEligibleDividends,
        EiDeduction,
        CppContribution,
        SelfEmployed
    }
}