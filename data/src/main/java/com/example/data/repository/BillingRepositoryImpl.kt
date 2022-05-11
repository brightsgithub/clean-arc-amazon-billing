package com.example.data.repository

import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.RequestId
import com.amazon.device.iap.model.UserDataResponse
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
    private val billingStorage: BillingStorage
    ): BillingRepository, PurchasingListener {

    private val continuationsMap = mutableMapOf<RequestId, Continuation<Unit>>()

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
                PurchasingService.purchase(sku).let { requestId ->
                    continuationsMap[requestId] = cont
                }
            }
        }
    }

    override fun onUserDataResponse(p0: UserDataResponse?) {
        TODO("Not yet implemented")
    }

    override fun onProductDataResponse(p0: ProductDataResponse?) {
        TODO("Not yet implemented")
    }

    override fun onPurchaseResponse(purchaseResponse: PurchaseResponse) {
        // if the request id exist in the map we resume the continuation
        continuationsMap[purchaseResponse.requestId]?.let { cont ->
            when (purchaseResponse.requestStatus) {
                PurchaseResponse.RequestStatus.SUCCESSFUL -> cont.resume(Unit)
                PurchaseResponse.RequestStatus.FAILED ->
                    cont.resumeWithException(BillingRepository.BillingError.PurchaseFailedException)
                PurchaseResponse.RequestStatus.INVALID_SKU ->
                    cont.resumeWithException(BillingRepository.BillingError.InvalidSkuException)
                PurchaseResponse.RequestStatus.ALREADY_PURCHASED -> TODO()
                PurchaseResponse.RequestStatus.NOT_SUPPORTED -> TODO()
            }
        }
    }

    override fun onPurchaseUpdatesResponse(p0: PurchaseUpdatesResponse?) {
        TODO("Not yet implemented")
    }
}