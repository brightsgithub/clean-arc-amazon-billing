package com.example.domain.usecase

import com.example.domain.repository.BillingRepository
import com.example.domain.usecase.interfaces.InitBillingUseCase
import kotlinx.coroutines.CoroutineScope

class InitBillingUseCaseImpl(private val billingRepository: BillingRepository): InitBillingUseCase {
    override fun invoke(coroutineScope: CoroutineScope): Boolean {
        return billingRepository.initBilling(coroutineScope)
    }
}