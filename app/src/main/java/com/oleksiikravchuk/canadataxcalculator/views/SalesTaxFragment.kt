package com.oleksiikravchuk.canadataxcalculator.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oleksiikravchuk.canadataxcalculator.R
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceArrayAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentSalesTaxBinding
import com.oleksiikravchuk.canadataxcalculator.models.Province
import com.oleksiikravchuk.canadataxcalculator.sales.SalesTax
import com.oleksiikravchuk.canadataxcalculator.utils.RatesAndAmounts2023
import com.oleksiikravchuk.canadataxcalculator.viewmodels.SaleItemUiState
import com.oleksiikravchuk.canadataxcalculator.viewmodels.SalesTaxViewModel

class SalesTaxFragment : Fragment() {

    lateinit var binding: FragmentSalesTaxBinding
    lateinit var viewModel: SalesTaxViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesTaxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SalesTaxViewModel::class.java]

        setSpinnerAdapter()
        renderUiState(viewModel.saleItemUiState.value)
        initListeners()
        initObservers()
    }

    private fun renderUiState(uiState: SaleItemUiState?) {
        if (uiState == null) {
            binding.textViewGstHstValue.text = getString(R.string.hst_value, "0.00")
            binding.textViewTotalAmount.text = getString(R.string.sales_total_amount, "0.00")
            return
        }

        binding.textViewTotalAmount.text =
            getString(R.string.sales_total_amount, String.format("%.2f", uiState.total))

        for (tax in uiState.taxesList) {
            when (tax.first) {
                SalesTax.SaleTaxesType.GST -> {
                    binding.textViewGstHstValue.visibility = View.VISIBLE
                    binding.textViewGstHstValue.text = getString(
                        R.string.gst_value,
                        String.format("%.2f", tax.second)
                    )
                    binding.textViewPstValue.visibility = View.INVISIBLE
                }
                SalesTax.SaleTaxesType.HST -> {
                    binding.textViewGstHstValue.visibility = View.VISIBLE
                    binding.textViewGstHstValue.text = getString(
                        R.string.hst_value,
                        String.format("%.2f", tax.second)
                    )
                    binding.textViewPstValue.visibility = View.INVISIBLE
                }
                SalesTax.SaleTaxesType.PST -> {
                    binding.textViewPstValue.visibility = View.VISIBLE
                    binding.textViewPstValue.text = getString(
                        R.string.pst_value,
                        String.format("%.2f", tax.second)
                    )
                }
                SalesTax.SaleTaxesType.QST -> {
                    binding.textViewPstValue.visibility = View.VISIBLE
                    binding.textViewPstValue.text = getString(R.string.qst_value, String.format("%.2f", tax.second))
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.saleItemUiState.observe(viewLifecycleOwner) { uiState ->
            renderUiState(uiState)
        }
    }

    private fun setSpinnerAdapter() {
        val provincesArray = RatesAndAmounts2023.provincesAndRates2023
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }

    private fun initListeners() {
        binding.editTextPriceBeforeTax.addTextChangedListener {
            if (it.isNullOrEmpty())
                viewModel.basePrice = 0.0
            else
                viewModel.basePrice = it.toString().toDouble()
        }
        binding.editTextDiscount.addTextChangedListener {
            if (it.isNullOrEmpty())
                viewModel.discount = 0.0
            else if (it.toString().toDouble() >= 100) {
                Toast.makeText(
                    this.context,
                    getString(R.string.discount_validation_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.discount = it.toString().toDouble()
            }
        }

        binding.spinnerProvinces.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setProvince(binding.spinnerProvinces.selectedItem as Province)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
    }
}