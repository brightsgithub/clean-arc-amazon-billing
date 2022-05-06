package com.example.amazonbillingexample.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.PurchaseUseCase

class AmazonBillingViewModel (
    private val purchaseUseCase: PurchaseUseCase
) : ViewModel() {

    fun purchase() {
        Log.v("AmazonBillingViewModel",purchaseUseCase.execute())
    }

}