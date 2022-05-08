package com.example.amazonbillingexample.views

import android.os.Bundle
import android.view.View.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.amazonbillingexample.R
import com.example.amazonbillingexample.models.PurchaseViewState
import com.example.amazonbillingexample.viewmodels.BillingViewModel
import com.example.domain.models.NowProductWrapper
import com.example.domain.models.NowSku
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val viewModel: BillingViewModel by viewModel()
    private lateinit var skus: List<NowSku>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                    is PurchaseViewState.GetProductSkusSuccess -> { skus = state.skus }
                    is PurchaseViewState.GetProductSkusFailure -> {}
                    is PurchaseViewState.HideLoading -> { hideLoadingView() }
                    is PurchaseViewState.ShowLoading -> { showLoadingView() }
                    is PurchaseViewState.ProductsLoadedFailure -> { populateInAppProductsText("No Products Found") }
                    is PurchaseViewState.ProductsLoadedSuccess -> { displayProducts(state.nowProductWrapper)}
                    is PurchaseViewState.ProductPurchasedFailure -> { showProductPurchasedFailure() }
                    is PurchaseViewState.ProductPurchasedSuccess -> { showProductPurchasedSuccess()}
                }
            }
        }
    }

    private fun populateInAppProductsText(msg: String) {
        iap_items_text.text = msg
    }

    private fun showLoadingView() {
        loading_spinner.visibility = VISIBLE
        loading_text.visibility = VISIBLE
        get_iap_button.isEnabled = false
        buy_apple.isEnabled = false
        buy_mango.isEnabled = false
    }

    private fun hideLoadingView() {
        loading_spinner.visibility = GONE
        loading_text.visibility = GONE
        get_iap_button.isEnabled = true
        buy_apple.isEnabled = true
        buy_mango.isEnabled = true
    }

    private fun showProductPurchasedSuccess() {
        Toast.makeText(this, "Purchase Success", Toast.LENGTH_SHORT).show()
    }

    private fun showProductPurchasedFailure() {
        Toast.makeText(this, "Purchase Failure", Toast.LENGTH_SHORT).show()
    }

    private fun displayProducts(nowProductWrapper: NowProductWrapper) {
        var products = StringBuilder("")

        nowProductWrapper.mapOfProducts.forEach { entry ->
            val nowProduct = entry.value
            products.append(nowProduct.skuId)
            products.append(" ")
            products.append(nowProduct.title)
            products.append("\n")
        }
        populateInAppProductsText(products.toString())
    }
}