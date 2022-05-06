package com.example.domain.usecase

import com.example.domain.models.BillingListenerEvent
import com.example.domain.repository.BillingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PurchaseUseCaseImpl(private val billingRepository: BillingRepository): PurchaseUseCase {

    override fun invoke(params: PurchaseUseCase.Params): Flow<BillingListenerEvent> {

        return billingRepository.listenForBillingEvents()
    }
}