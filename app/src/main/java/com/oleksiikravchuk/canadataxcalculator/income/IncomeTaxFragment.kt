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
    private lateinit var incomeTax : IncomeTax

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
    }

    private fun setUI() {
        setSpinnerAdapter()
        binding.buttonCalculateTaxes.setOnClickListener { calculateTaxes() }
    }


    private fun calculateTaxes() {

        if(binding.editTextAnnualIncome.text.isNullOrEmpty()) {
            Toast.makeText(context, "Enter Annual Income", Toast.LENGTH_LONG).show()
            return
        }

        incomeTax = IncomeTax( Province("Alberta", 0),
            binding.editTextAnnualIncome.text.toString().toDouble())

        binding.textViewFederal1Tax.text = incomeTax.getFederalTax().toString()
    }

    private fun setSpinnerAdapter() {
        val provincesArray = arrayOf(
            Province("Alberta", R.drawable.flag_of_alberta),
            Province("British Columbia", R.drawable.flag_of_british_columbia),
            Province("Manitoba", R.drawable.flag_of_manitoba),
            Province("New Brunswick", R.drawable.flag_of_new_brunswick),
            Province("Newfoundland and Labrador", R.drawable.flag_of_newfoundland_and_labrador),
            Province("Northwest Territories", R.drawable.flag_of_the_northwest_territories),
            Province("Nova Scotia", R.drawable.flag_of_nova_scotia),
            Province("Nunavut", R.drawable.flag_of_nunavut),
            Province("Ontario", R.drawable.flag_of_ontario),
            Province("Prince Edward Island", R.drawable.flag_of_prince_edward_island),
            Province("Quebec", R.drawable.flag_of_quebec),
            Province("Saskatchewan", R.drawable.flag_of_saskatchewan),
            Province("Yukon", R.drawable.flag_of_yukon),
        )
        binding.spinnerProvinces.adapter = ProvinceArrayAdapter(provincesArray)
    }


}