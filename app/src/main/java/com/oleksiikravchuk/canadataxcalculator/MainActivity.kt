package com.oleksiikravchuk.canadataxcalculator

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import com.oleksiikravchuk.canadataxcalculator.databinding.ActivityMainBinding
import com.oleksiikravchuk.canadataxcalculator.income.IncomeTaxFragment
import com.oleksiikravchuk.canadataxcalculator.sales.SalesTaxFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
    }

    private fun setUI() {
        binding.bottomNavigationView.setOnItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.nav_income -> onIncomeTaxClicked()
        R.id.nav_sales  -> onSalesTaxClicked()
        else -> false
    }

    private fun onSalesTaxClicked(): Boolean {
        supportFragmentManager.commit{
            replace(R.id.fragment_container_view, SalesTaxFragment())
        }
        return true
    }

    private fun onIncomeTaxClicked(): Boolean {
        supportFragmentManager.commit{
            replace(R.id.fragment_container_view, IncomeTaxFragment())
        }
        return true
    }
}