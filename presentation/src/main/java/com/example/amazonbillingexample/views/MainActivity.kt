package com.example.amazonbillingexample.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.amazonbillingexample.R
import com.example.amazonbillingexample.models.PurchaseViewState
import com.example.amazonbillingexample.viewmodels.BillingViewModel
import com.example.domain.models.NowSku
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: BillingViewModel by viewModel()
    private lateinit var skus: List<NowSku>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.initBilling()
    }

    override fun onStart() {
        super.onStart()
        observeViewState()
    }

    private fun observeViewState() {
        GlobalScope.launch {
            viewModel.listenForViewUpdates().collect { state ->
                when(state) {
                    is PurchaseViewState.GetProductSkusSuccess -> {
                        skus = state.skus
                    }
                    is PurchaseViewState.GetProductSkusFailure -> {}
                    is PurchaseViewState.HideLoading -> {}
                    is PurchaseViewState.ShowLoading -> {}
                    is PurchaseViewState.ProductsLoadedFailure -> {}
                    is PurchaseViewState.ProductsLoadedSuccess -> { displayProducts()}
                }
            }
        }
    }

    private fun displayProducts() {
        // display products on the view
    }

    override fun onResume() {
        super.onResume()
        //viewModel.purchase("111")
        viewModel.loadProducts()
    }
}