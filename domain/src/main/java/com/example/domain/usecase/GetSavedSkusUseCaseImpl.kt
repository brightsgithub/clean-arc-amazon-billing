package com.example.domain.usecase

import com.example.domain.exceptions.NoSavedSkusFoundException
import com.example.domain.models.NowSku
import com.example.domain.repository.BillingRepository
import com.example.domain.usecase.interfaces.GetSavedSkusUseCase
import kotlin.jvm.Throws

class GetSavedSkusUseCaseImpl(private val billingRepository: BillingRepository):
    GetSavedSkusUseCase {

    @Throws(NoSavedSkusFoundException::class)
    override suspend fun invoke(): List<NowSku> {
        val skus = billingRepository.getSavedSkus()
        if (skus.isEmpty()) {
            throw NoSavedSkusFoundException()
        }
        return skus
    }
}