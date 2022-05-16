package com.example.data.repository

interface BillingService {

    fun setListener(listener: Listener)

    fun purchaseItem(sku: String): String

    interface Listener {
        fun onBillingPurchaseResponse(billingPurchaseResponse: BillingPurchaseResponse)
    }

    data class BillingPurchaseResponse(val id: String?, val status: Status)

    enum class Status {
        SUCCESSFUL,
        FAILED,
        INVALID_SKU,
        ALREADY_PURCHASED,
        NOT_SUPPORTED
    }
}