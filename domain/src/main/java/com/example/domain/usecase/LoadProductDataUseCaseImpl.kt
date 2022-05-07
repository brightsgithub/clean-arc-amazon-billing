package com.example.domain.usecase

import com.example.domain.repository.BillingRepository
import com.example.domain.usecase.interfaces.LoadProductDataUseCase

class LoadProductDataUseCaseImpl(
    private val billingRepository: BillingRepository
): LoadProductDataUseCase {

    override suspend fun invoke() {
        return billingRepository.loadProductData()
    }
}