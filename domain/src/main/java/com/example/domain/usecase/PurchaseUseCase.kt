package com.example.domain.usecase

import com.example.domain.models.BillingListenerEvent
import kotlinx.coroutines.flow.Flow

interface PurchaseUseCase: UseCase<PurchaseUseCase.Params, Flow<BillingListenerEvent>> {
    data class Params(var sku: String)
}