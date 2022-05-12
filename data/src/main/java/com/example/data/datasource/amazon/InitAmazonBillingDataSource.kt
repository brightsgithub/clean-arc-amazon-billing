package com.example.data.datasource.amazon

import android.content.Context
import android.util.Log
import com.amazon.device.drm.LicensingService

import com.amazon.device.iap.PurchasingService
import com.example.data.datasource.BillingListenerDataSource
import com.example.data.datasource.InitBillingDataSource
import kotlinx.coroutines.CoroutineScope

class InitAmazonBillingDataSource(
    private val billingListenerDataSource: BillingListenerDataSource,
    private val applicationContext: Context
    ): InitBillingDataSource {

    override fun initBilling(coroutineScope: CoroutineScope) : Boolean {
        Log.d("InitBilling", "onCreate: registering PurchasingListener")
        billingListenerDataSource.initCurrentScope(coroutineScope)
        PurchasingService.registerListener(applicationContext, billingListenerDataSource)
        Log.d("InitBilling","Appstore SDK Mode: " + LicensingService.getAppstoreSDKMode()) // Checks if app is in test mode
        return true
    }
}