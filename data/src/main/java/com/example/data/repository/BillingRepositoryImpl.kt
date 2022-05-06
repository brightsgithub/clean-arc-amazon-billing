package com.example.data.repository

import com.example.data.datasource.BillingListenerDataSource
import com.example.data.datasource.InitBillingDataSource
import com.example.data.datasource.ProcessBillingRequestsDataSource
import com.example.domain.models.BillingListenerEvent
import com.example.domain.repository.BillingRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class BillingRepositoryImpl(
    private val billingListenerDataSource: BillingListenerDataSource,
    private val initBillingDataSource: InitBillingDataSource,
    private val processBillingRequest: ProcessBillingRequestsDataSource
    ): BillingRepository {

    override fun initBilling(): Boolean {
        return initBillingDataSource.initBilling()
    }

    override fun listenForBillingEvents(): Flow<BillingListenerEvent> {
        return billingListenerDataSource.listenForBillingEvents()
    }

    override fun purchaseItem(sku: String) {
        processBillingRequest.purchaseItem(sku)
    }
}