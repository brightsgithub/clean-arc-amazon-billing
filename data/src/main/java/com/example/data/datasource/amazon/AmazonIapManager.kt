package com.example.data.datasource.amazon

import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.FulfillmentResult
import com.amazon.device.iap.model.Receipt
import com.amazon.device.iap.model.UserData
import com.example.data.storage.BillingStorage

class AmazonIapManager(private val billingStorage: BillingStorage) {

    fun handleConsumablePurchase(receipt: Receipt, userData: UserData): Boolean {
        try {
                // We strongly recommend that you verify the receipt server-side
                if (!verifyReceiptFromYourService(receipt.receiptId, userData)) {
                    return false
                }
            PurchasingService.notifyFulfillment(receipt.receiptId, FulfillmentResult.FULFILLED)
            return true
        } catch (e: Throwable) {
            return false
        }
    }

    /**
     * We strongly recommend verifying the receipt on your own server side
     * first. The server side verification ideally should include checking with
     * Amazon RVS (Receipt Verification Service) to verify the receipt details.
     *
     * @see [Appstore's Receipt Verification Service](https://developer.amazon.com/appsandservices/apis/earn/in-app-purchasing/docs/rvs)
     *
     *
     * @param receiptId
     * @return
     */
    private fun verifyReceiptFromYourService(receiptId: String, userData: UserData): Boolean {
        // TODO Add your own server side accessing and verification code
        return true
    }
}