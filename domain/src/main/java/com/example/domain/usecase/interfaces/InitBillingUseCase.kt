package com.example.domain.usecase.interfaces

import kotlinx.coroutines.CoroutineScope

interface InitBillingUseCase {
    fun invoke(coroutineScope: CoroutineScope): Boolean
}