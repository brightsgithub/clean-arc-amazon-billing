package com.example.amazonbillingexample.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.amazonbillingexample.R
import com.example.amazonbillingexample.viewmodels.BillingViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: BillingViewModel by viewModel()
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
                Log.v("BillingViewModel", "MainActivity " + state)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.purchase("111")
    }
}