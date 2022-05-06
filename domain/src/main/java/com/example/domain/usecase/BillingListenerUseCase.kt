package com.example.domain.usecase

import com.example.domain.models.BillingListenerEvent
import kotlinx.coroutines.flow.Flow

interface BillingListenerUseCase: UseCaseUnit<Flow<BillingListenerEvent>>