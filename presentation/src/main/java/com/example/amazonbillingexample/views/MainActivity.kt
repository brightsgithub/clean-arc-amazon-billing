package com.example.amazonbillingexample.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.amazonbillingexample.R
import com.example.amazonbillingexample.viewmodels.AmazonBillingViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: AmazonBillingViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.purchase()
    }
}