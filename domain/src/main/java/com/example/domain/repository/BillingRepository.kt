package com.example.domain.repository

import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowSku
import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    fun listenForBillingEvents(): Flow<BillingListenerEvent>
    suspend fun purchaseItem(sku: String)
    fun initBilling(): Boolean
    fun getSavedSkus(): List<NowSku>
    fun loadProductData()

    sealed class BillingError: Exception() {
        object PurchaseFailedException : BillingError()
        object InvalidSkuException: BillingError()
    }
}