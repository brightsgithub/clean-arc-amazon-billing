package com.example.amazonbillingexample.views

import android.app.UiModeManager
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.amazonbillingexample.R
import com.example.amazonbillingexample.models.PurchaseViewState
import com.example.amazonbillingexample.viewmodels.BillingViewModel
import com.example.domain.models.NowSku
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: BillingViewModel by viewModel()
    private lateinit var skus: List<NowSku>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setupToolbar()
        setupButtonClickListeners()
        viewModel.initBilling()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedSkus()
    }

    private fun setupButtonClickListeners() {
        get_iap_button.setOnClickListener {
            viewModel.loadProducts()
        }

        buy_apple.setOnClickListener {
            val skuApple = viewModel.searchForSKU(skus, "apple")
            viewModel.purchase(skuApple)
        }

        buy_mango.setOnClickListener {
            val skuMango = viewModel.searchForSKU(skus, "mango")
            viewModel.purchase(skuMango)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(app_toolbar)
        // Display application icon in the toolbar
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }

    override fun onStart() {
        super.onStart()
        observeViewState()
    }

    private fun observeViewState() {

        lifecycleScope.launch {
            viewModel.listenForViewUpdates().collect { state ->
                when(state) {
                    is PurchaseViewState.GetProductSkusSuccess -> {
                        skus = state.skus
                    }
                    is PurchaseViewState.GetProductSkusFailure -> {}
                    is PurchaseViewState.HideLoading -> { hideLoadingView() }
                    is PurchaseViewState.ShowLoading -> { showLoadingView() }
                    is PurchaseViewState.ProductsLoadedFailure -> {}
                    is PurchaseViewState.ProductsLoadedSuccess -> { displayProducts()}
                }
            }
        }
    }

    private fun showLoadingView() {
        loading_spinner.visibility = VISIBLE
        get_iap_button.isEnabled = false
    }

    private fun hideLoadingView() {
        loading_spinner.visibility = INVISIBLE
        get_iap_button.isEnabled = true
    }


    private fun displayProducts() {
        // display products on the view
    }

}