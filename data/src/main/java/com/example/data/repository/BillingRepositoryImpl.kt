package com.example.data.repository

import com.example.data.datasource.BillingListenerDataSource
import com.example.data.datasource.InitBillingDataSource
import com.example.data.datasource.ProcessBillingRequestsDataSource
import com.example.data.storage.BillingStorage
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowSku
import com.example.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * This class should remain generic & apply to ALL types of Billing implementations.
 * Specifics should be kept inside respective datasources.
 */
class BillingRepositoryImpl(
    private val billingListenerDataSource: BillingListenerDataSource,
    private val initBillingDataSource: InitBillingDataSource,
    private val processBillingRequest: ProcessBillingRequestsDataSource,
    private val billingStorage: BillingStorage,
    private val billingService: BillingServiceAmazon
    ): BillingRepository {

    init {
        billingService.setListener(listener = object : BillingService.Listener {
            override fun onBillingPurchaseResponse(billingPurchaseResponse: BillingService.BillingPurchaseResponse) {
                continuationsMap[billingPurchaseResponse.id]?.let { cont ->
                    when (billingPurchaseResponse.status) {
                        BillingService.Status.SUCCESSFUL -> cont.resume(Unit)
                        BillingService.Status.FAILED ->
                            cont.resumeWithException(BillingRepository.BillingError.PurchaseFailedException)
                        BillingService.Status.INVALID_SKU ->
                            cont.resumeWithException(BillingRepository.BillingError.InvalidSkuException)
                        BillingService.Status.ALREADY_PURCHASED -> TODO()
                        BillingService.Status.NOT_SUPPORTED -> TODO()
                    }
                }
            }
        })
    }

    private val continuationsMap = mutableMapOf<String, Continuation<Unit>>() // key is request id

    override fun initBilling(): Boolean {
        return initBillingDataSource.initBilling()
    }

    override fun getSavedSkus(): List<NowSku> {
        return billingStorage.getSavedProductSkus()
    }

    override fun loadProductData() {
        processBillingRequest.getProductData()
    }

    override fun listenForBillingEvents(): Flow<BillingListenerEvent> {
        return billingListenerDataSource.listenForBillingEvents()
    }

    override suspend fun purchaseItem(sku: String) {
        withTimeout(5000) {
            suspendCoroutine<Unit> { cont ->
                billingService.purchaseItem(sku).let {
                    continuationsMap[it] = cont
                }
            }
        }
    }
}