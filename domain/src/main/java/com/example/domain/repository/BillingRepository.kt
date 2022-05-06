package com.example.domain.repository

import com.example.domain.models.BillingListenerEvent
import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    fun listenForBillingEvents(): Flow<BillingListenerEvent>
    fun purchaseItem(sku: String)
}