package com.example.data.repository

import com.example.data.datasource.BillingListenerDataSource
import com.example.data.datasource.InitBillingDataSource
import com.example.data.datasource.ProcessBillingRequestsDataSource
import com.example.data.storage.BillingStorage
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowSku
import com.example.domain.repository.BillingRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * This class should remain generic & apply to ALL types of Billing implementations.
 * Specifics should be kept inside respective datasources.
 */
class BillingRepositoryImpl(
    private val billingListenerDataSource: BillingListenerDataSource,
    private val initBillingDataSource: InitBillingDataSource,
    private val processBillingRequest: ProcessBillingRequestsDataSource,
    private val billingStorage: BillingStorage
    ): BillingRepository {

    override fun initBilling(): Boolean {
        return initBillingDataSource.initBilling()
    }

    override suspend fun getSavedSkus(): List<NowSku> {
        return billingStorage.getSavedProductSkus()
    }

    override suspend fun loadProductData() {
        val savedSkus = getSavedSkus()
        processBillingRequest.getProductData(savedSkus)
    }

    override fun listenForBillingEvents(): Flow<BillingListenerEvent> {
        return billingListenerDataSource.listenForBillingEvents()
    }

    override fun purchaseItem(sku: String) {
        processBillingRequest.purchaseItem(sku)
    }
}