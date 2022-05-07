package com.example.domain.usecase.interfaces

interface PurchaseUseCase {
    data class Params(var sku: String)

    fun invoke(params: Params)
}