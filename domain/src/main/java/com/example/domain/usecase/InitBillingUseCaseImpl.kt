package com.example.domain.usecase

import com.example.domain.repository.BillingRepository

class InitBillingUseCaseImpl(private val billingRepository: BillingRepository): InitBillingUseCase {
    override fun invoke(): Boolean {
        return billingRepository.initBilling()
    }
}