package com.example.domain.usecase.interfaces

interface PurchaseUseCase {
    data class Params(var sku: String)

    suspend fun invoke(params: Params)
}