package com.example.domain.repository

import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowSku
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    fun listenForBillingEvents(): Flow<BillingListenerEvent>
    fun purchaseItem(sku: String)
    fun initBilling(coroutineScope: CoroutineScope): Boolean
    fun unRegisterBilling()
    fun getSavedSkus(): List<NowSku>
    fun loadProductData()
}