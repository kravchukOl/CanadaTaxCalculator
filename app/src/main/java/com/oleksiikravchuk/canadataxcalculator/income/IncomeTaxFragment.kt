package com.oleksiikravchuk.canadataxcalculator.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.oleksiikravchuk.canadataxcalculator.IncomeTax
import com.oleksiikravchuk.canadataxcalculator.Province
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceArrayAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentIncomeTaxBinding

class IncomeTaxFragment : Fragment() {

    private lateinit var binding: FragmentIncomeTaxBinding
    private var incomeTax = IncomeTax()

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

        if( binding.tableLayoutSummary.visibility == View.VISIBLE ) {
            outState.putInt("Summary Table Visibility", binding.tableLayoutSummary.visibility)
            outState.putString("Federal Tax", binding.textViewFederalTax.text.toString())
            outState.putString("Provincial Tax", binding.textViewProvincialTax.text.toString())
            outState.putString("Total Income Tax", binding.textViewTotalIncomeTax.text.toString())
            outState.putString("Net Income", binding.textViewNetIncome.text.toString())
            outState.putString("Average Tax Rate", binding.textViewAverageTaxRate.text.toString())
        }
    }

    private fun applySavedInstanceStates(instanceState: Bundle) {
        binding.editTextAnnualIncome.setText(instanceState.getString("Annual Income"))

        if(instanceState.getInt("Summary Table Visibility") == View.VISIBLE) {
            binding.tableLayoutSummary.visibility = View.VISIBLE
            binding.textViewFederalTax.text = instanceState.getString("Federal Tax")
            binding.textViewProvincialTax.text = instanceState.getString("Provincial Tax")
            binding.textViewNetIncome.text = instanceState.getString("Total Income Tax")
            binding.textViewNetIncome.text = instanceState.getString("Net Income")
            binding.textViewAverageTaxRate.text = instanceState.getString("Average Tax Rate")

        }
    }

    private fun setUI() {
        setSpinnerAdapter()
        binding.buttonCalculateTaxes.setOnClickListener {
            calculateTaxes()
        }
    }


    private fun calculateTaxes() {

        if (binding.editTextAnnualIncome.text.isNullOrEmpty()) {
            Toast.makeText(context, "Enter Annual Income", Toast.LENGTH_LONG).show()
        } else {
            binding.tableLayoutSummary.visibility = View.VISIBLE

            val annualIncome = binding.editTextAnnualIncome.text.toString().toDouble()
            val federalTax = incomeTax.getFederalTax(annualIncome)
            val provinceTax = incomeTax.getProvinceTax(
                annualIncome,
                binding.spinnerProvinces.selectedItem as Province
            )

            binding.textViewFederalTax.text =
                String.format("%.2f ${getString(R.string.input_card_currency_cad)}", federalTax)

            binding.textViewProvincialTax.text =
                String.format("%.2f ${getString(R.string.input_card_currency_cad)}", provinceTax)

            binding.textViewTotalIncomeTax.text =
                String.format(
                    "%.2f ${getString(R.string.input_card_currency_cad)}",
                    provinceTax + federalTax
                )

            binding.textViewNetIncome.text =
                String.format(
                    "%.2f ${getString(R.string.input_card_currency_cad)}",
                    annualIncome - provinceTax - federalTax
                )

            binding.textViewAverageTaxRate.text =
                String.format("%.1f%%", (provinceTax + federalTax) / annualIncome * 100)
        }
    }

    private fun setSpinnerAdapter() {
        val provincesArray = incomeTax.getIndividualsIncomeTaxRates()
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }


}