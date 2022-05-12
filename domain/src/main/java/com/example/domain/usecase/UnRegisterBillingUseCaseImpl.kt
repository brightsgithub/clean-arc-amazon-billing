package com.example.domain.usecase

import com.example.domain.repository.BillingRepository
import com.example.domain.usecase.interfaces.UnRegisterBillingUseCase

class UnRegisterBillingUseCaseImpl(private val billingRepository: BillingRepository): UnRegisterBillingUseCase {
    override fun invoke() {
        billingRepository.unRegisterBilling()
    }
}