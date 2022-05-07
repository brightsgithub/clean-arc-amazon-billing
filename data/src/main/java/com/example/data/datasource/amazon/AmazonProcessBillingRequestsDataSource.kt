package com.example.data.datasource.amazon

import android.util.Log
import com.amazon.device.iap.PurchasingService
import com.example.data.datasource.ProcessBillingRequestsDataSource
import com.example.domain.models.NowSku

class AmazonProcessBillingRequestsDataSource: ProcessBillingRequestsDataSource {

    private val TAG = "BillingRequest"
    override fun getUserData() {
        PurchasingService.getUserData()
    }

    override fun getPurchaseInfo() {
        PurchasingService.getPurchaseUpdates(true)
    }

    override fun getProductData(skus: List<NowSku>) {
        Log.v(TAG, "getProductData()")
        Log.v(TAG, "Printing skus to be sent to PurchasingService.getProductData(skuSet)...")
        val skuSet = hashSetOf<String>()
        for (sku in skus) {
            skuSet.add(sku.skuId)
            Log.v(TAG, "SKU "+ sku.skuId)
        }
        Log.v(TAG, "Printing skus to be sent to PurchasingService.getProductData(skuSet) FINISHED")
        PurchasingService.getProductData(skuSet)
    }

    override fun purchaseItem(sku: String) {
        PurchasingService.purchase(sku)
    }
}