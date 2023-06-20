package com.oleksiikravchuk.canadataxcalculator.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.oleksiikravchuk.canadataxcalculator.adapters.ProvinceArrayAdapter
import com.oleksiikravchuk.canadataxcalculator.databinding.FragmentSalesTaxBinding
import com.oleksiikravchuk.canadataxcalculator.utils.RatesAndAmounts2023
import com.oleksiikravchuk.canadataxcalculator.viewmodels.SalesTaxViewModel

class SalesTaxFragment : Fragment() {

    lateinit var binding: FragmentSalesTaxBinding
    lateinit var viewModel : SalesTaxViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesTaxBinding.inflate(inflater, container,  false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SalesTaxViewModel::class.java]

        setSpinnerAdapter()
    }

    private fun setSpinnerAdapter() {
        val provincesArray = RatesAndAmounts2023.provincesAndRates2023
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }
}