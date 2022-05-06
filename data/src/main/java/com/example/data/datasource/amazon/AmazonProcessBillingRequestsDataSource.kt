package com.example.data.datasource.amazon

import com.amazon.device.iap.PurchasingService
import com.example.data.datasource.ProcessBillingRequestsDataSource

class AmazonProcessBillingRequestsDataSource: ProcessBillingRequestsDataSource {

    override fun getUserData() {
        PurchasingService.getUserData()
    }

    override fun getPurchaseInfo() {
        PurchasingService.getPurchaseUpdates(true)
    }

    override fun getProductData(skus: Set<String>) {
        PurchasingService.getProductData(skus)
    }

    override fun purchaseItem(sku: String) {
        PurchasingService.purchase(sku)
    }
}