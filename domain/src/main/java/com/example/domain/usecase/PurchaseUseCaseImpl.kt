package com.example.domain.usecase

import com.example.domain.repository.BillingRepository

class PurchaseUseCaseImpl(private val billingRepository: BillingRepository): PurchaseUseCase {

    override fun invoke(params: PurchaseUseCase.Params) {
        return billingRepository.purchaseItem(params.sku)
    }
}