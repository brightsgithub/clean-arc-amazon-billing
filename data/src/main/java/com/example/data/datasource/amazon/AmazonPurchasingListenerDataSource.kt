package com.example.data.datasource.amazon

import android.util.Log
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import com.example.data.datasource.BillingListenerDataSource
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowProduct
import com.example.domain.models.NowProductWrapper
import com.example.domain.models.NowUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Every call you initiate via the PurchasingService results in a corresponding response received
 * by the PurchasingListener. Each of these responses uses a response object:
 */
class AmazonPurchasingListenerDataSource(private val amazonIapManager: AmazonIapManager): BillingListenerDataSource {

    // We are working with whatever scope that was provided by the client.
    // If the client cancels the scope, then nothing is pushed
    // also allows us to use MutableSharedFlow (for submitting the same responses, such as multiple purchases)
    // which allows us to avoid using Global.Scope which is bad practice
    private var currentScope: CoroutineScope? = null
    private val _billingEventSubject = MutableSharedFlow<BillingListenerEvent?>()
    private val billingEventSubject: Flow<BillingListenerEvent?> = _billingEventSubject
    private val TAG = "AmazonPurchaseListener"

    override fun listenForBillingEvents(): Flow<BillingListenerEvent> {
        return billingEventSubject.filterNotNull()
    }

    override fun initCurrentScope(scope: CoroutineScope) {
        this.currentScope = scope
    }

    override fun unRegisterBilling() {
        this.currentScope = null
    }

    /**
     * onUserDataResponse(UserDataResponse userDataResponse): Invoked after a call to getUserData.
     * Determines the UserId and marketplace of the currently logged on user.
     */
    override fun onUserDataResponse(response: UserDataResponse) {
        currentScope?.let {
            it.launch {
                when (response.requestStatus) {
                    UserDataResponse.RequestStatus.SUCCESSFUL -> {
                        _billingEventSubject.emit(BillingListenerEvent.GetUserDataEventSuccess(NowUserData(response!!.userData.userId)))
                    }
                    UserDataResponse.RequestStatus.FAILED -> {
                        _billingEventSubject.emit(BillingListenerEvent.GetUserDataEventFailure)
                    }
                    UserDataResponse.RequestStatus.NOT_SUPPORTED -> {
                        _billingEventSubject.emit(BillingListenerEvent.GetUserDataEventFailure)
                    }
                }
            }
        }
    }

    /**
     * onProductDataResponse(ProductDataResponse productDataResponse): Invoked after a call to
     * getProductDataRequest(java.util.Set skus). Retrieves information about SKUs you would like
     * to sell from your app. Use the valid SKUs in onPurchaseResponse.
     */
    override fun onProductDataResponse(response: ProductDataResponse) {
        currentScope?.let {
            it.launch {
                when (response.requestStatus) {
                    ProductDataResponse.RequestStatus.SUCCESSFUL -> {
                        val productsMap = hashMapOf<String, NowProduct>()
                        for (product in response.productData) {
                            val skuId = product.value.sku
                            val title = product.value.title
                            val description = product.value.description
                            val price = product.value.price
                            val nowProduct = NowProduct(skuId, title, description, price)
                            productsMap[skuId] = nowProduct
                        }
                        _billingEventSubject.emit(
                            BillingListenerEvent.GetProductDataEventSuccess(
                                NowProductWrapper(
                                    productsMap
                                )
                            )
                        )
                    }
                    ProductDataResponse.RequestStatus.FAILED -> {
                        _billingEventSubject.emit(BillingListenerEvent.GetProductDataEventFailure)
                    }
                    ProductDataResponse.RequestStatus.NOT_SUPPORTED -> {
                        _billingEventSubject.emit(BillingListenerEvent.GetProductDataEventFailure)
                    }
                }
            }
        }
    }

    /**
     * onPurchaseResponse(PurchaseResponse purchaseResponse): Invoked after a call to
     * purchase(String sku). Used to determine the status of a purchase.
     */
    override fun onPurchaseResponse(response: PurchaseResponse) {

        val requestId = response.requestId.toString()
        val userId = response.userData.userId

        currentScope?.let {
            it.launch {
                when (response.requestStatus) {
                    PurchaseResponse.RequestStatus.SUCCESSFUL -> {
                        val receipt = response.receipt // do some stuff with receipt
                        val userData = response.userData;
                        if (amazonIapManager.handleConsumablePurchase(receipt, userData)) {
                            _billingEventSubject.emit(BillingListenerEvent.PurchaseSuccessEvent)
                        } else {
                            _billingEventSubject.emit(BillingListenerEvent.FailedToPurchaseEvent)
                        }
                    }
                    PurchaseResponse.RequestStatus.FAILED -> {
                        _billingEventSubject.emit(BillingListenerEvent.FailedToPurchaseEvent)
                    }
                }
            }
        }
    }

    /**
     * onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse): Invoked after a
     * call to getPurchaseUpdates(boolean reset). Retrieves the purchase history.
     * Amazon recommends that you persist the returned PurchaseUpdatesResponse data
     * and query the system only for updates.
     */
    override fun onPurchaseUpdatesResponse(p0: PurchaseUpdatesResponse?) {
        Log.v(TAG, "onPurchaseUpdatesResponse")
    }
}