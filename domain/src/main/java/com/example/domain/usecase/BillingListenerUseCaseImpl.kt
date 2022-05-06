package com.example.domain.usecase

import com.example.domain.models.BillingListenerEvent
import com.example.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow

class BillingListenerUseCaseImpl(
    private val billingRepository: BillingRepository
    ): BillingListenerUseCase {

    override fun invoke(): Flow<BillingListenerEvent> {
        return billingRepository.listenForBillingEvents()
    }
}