package com.example.domain.usecase

import com.example.domain.models.BillingListenerEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface PurchaseUseCase {
    data class Params(var sku: String)

    fun invoke(params: Params)
}