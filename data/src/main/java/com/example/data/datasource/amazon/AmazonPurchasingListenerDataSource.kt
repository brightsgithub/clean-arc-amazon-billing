package com.example.data.datasource.amazon

import android.util.Log
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import com.example.data.datasource.BillingListenerDataSource
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowProduct
import com.example.domain.models.NowProductWrapper
import com.example.domain.models.NowUserData
import kotlinx.coroutines.flow.*

/**
 * Every call you initiate via the PurchasingService results in a corresponding response received
 * by the PurchasingListener. Each of these responses uses a response object:
 */
class AmazonPurchasingListenerDataSource(): BillingListenerDataSource {

    private val _billingEventSubject = MutableStateFlow<BillingListenerEvent?>(null)
    private val billingEventSubject: Flow<BillingListenerEvent?> = _billingEventSubject
    private val TAG = "AmazonPurchaseListener"

    override fun listenForBillingEvents(): Flow<BillingListenerEvent> {
        return billingEventSubject.filterNotNull()
    }

    /**
     * onUserDataResponse(UserDataResponse userDataResponse): Invoked after a call to getUserData.
     * Determines the UserId and marketplace of the currently logged on user.
     */
    override fun onUserDataResponse(response: UserDataResponse) {
        Log.v(TAG, "onUserDataResponse")
        when (response.requestStatus) {
            UserDataResponse.RequestStatus.SUCCESSFUL -> {
                _billingEventSubject.value =
                    BillingListenerEvent.GetUserDataEventSuccess(NowUserData(response!!.userData.userId))
            }
            UserDataResponse.RequestStatus.FAILED -> {
                _billingEventSubject.value = BillingListenerEvent.GetUserDataEventFailure
            }
            UserDataResponse.RequestStatus.NOT_SUPPORTED -> {
                _billingEventSubject.value = BillingListenerEvent.GetUserDataEventFailure
            }
        }
    }

    /**
     * onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse): Invoked after a
     * call to getPurchaseUpdates(boolean reset). Retrieves the purchase history.
     * Amazon recommends that you persist the returned PurchaseUpdatesResponse data
     * and query the system only for updates.
     */
    override fun onProductDataResponse(response: ProductDataResponse) {
        Log.v(TAG, "onProductDataResponse")
        when (response.requestStatus) {
            ProductDataResponse.RequestStatus.SUCCESSFUL -> {
                val productsMap = hashMapOf<String, NowProduct>()
                for(product in response.productData){
                    val skuId = product.value.sku
                    val title = product.value.title
                    val description = product.value.description
                    val price = product.value.price
                    val nowProduct = NowProduct(skuId, title, description, price)
                    productsMap[skuId] = nowProduct
                }
                _billingEventSubject.value =
                    BillingListenerEvent.GetProductDataEventSuccess(NowProductWrapper(productsMap))
            }
            ProductDataResponse.RequestStatus.FAILED -> {
                _billingEventSubject.value = BillingListenerEvent.GetProductDataEventFailure
            }
            ProductDataResponse.RequestStatus.NOT_SUPPORTED -> {
                _billingEventSubject.value = BillingListenerEvent.GetProductDataEventFailure
            }
        }
    }

    /**
     * onProductDataResponse(ProductDataResponse productDataResponse): Invoked after a call to
     * getProductDataRequest(java.util.Set skus). Retrieves information about SKUs you would like
     * to sell from your app. Use the valid SKUs in onPurchaseResponse.
     */
    override fun onPurchaseResponse(response: PurchaseResponse) {
        Log.v(TAG, "onPurchaseResponse " + Thread.currentThread().name)
        when (response.requestStatus) {
            PurchaseResponse.RequestStatus.SUCCESSFUL -> {
                val receipt  = response.receipt // do some stuff with receipt
                _billingEventSubject.value = BillingListenerEvent.PurchaseSuccessEvent
            }
            PurchaseResponse.RequestStatus.ALREADY_PURCHASED -> {
                _billingEventSubject.value = BillingListenerEvent.AlreadyPurchasedEvent

            }
            PurchaseResponse.RequestStatus.FAILED -> {
                _billingEventSubject.value = BillingListenerEvent.FailedToPurchaseEvent
            }
        }
    }

    /**
     * onPurchaseResponse(PurchaseResponse purchaseResponse): Invoked after a call to
     * purchase(String sku). Used to determine the status of a purchase.
     */
    override fun onPurchaseUpdatesResponse(p0: PurchaseUpdatesResponse?) {
        Log.v(TAG, "onPurchaseUpdatesResponse")
    }
}