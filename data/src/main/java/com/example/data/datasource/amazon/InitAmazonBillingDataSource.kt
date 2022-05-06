package com.example.data.datasource.amazon

import android.content.Context
import android.util.Log
import com.amazon.device.drm.LicensingService

import com.amazon.device.iap.PurchasingService
import com.example.data.datasource.BillingListenerDataSource
import com.example.data.datasource.InitBillingDataSource

class InitAmazonBillingDataSource(
    private val billingListenerDataSource: BillingListenerDataSource,
    private val applicationContext: Context
    ): InitBillingDataSource {

    override fun initBilling() : Boolean {
        Log.d("InitBilling", "onCreate: registering PurchasingListener")
        PurchasingService.registerListener(applicationContext, billingListenerDataSource)
        Log.d("InitBilling","Appstore SDK Mode: " + LicensingService.getAppstoreSDKMode()) // Checks if app is in test mode
        return true
    }
}