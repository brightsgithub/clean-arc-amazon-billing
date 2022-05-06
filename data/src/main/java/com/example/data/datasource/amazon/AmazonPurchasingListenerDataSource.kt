package com.example.data.datasource.amazon

import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse
import com.example.data.datasource.BillingListenerDataSource
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowProduct
import com.example.domain.models.NowProductWrapper
import com.example.domain.models.NowUserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

/**
 * Every call you initiate via the PurchasingService results in a corresponding response received
 * by the PurchasingListener. Each of these responses uses a response object:
 */
class AmazonPurchasingListenerDataSource: BillingListenerDataSource {

    private val _billingEventSubject = MutableStateFlow<BillingListenerEvent?>(null)
    private val billingEventSubject: Flow<BillingListenerEvent?> = _billingEventSubject

    override fun listenForBillingEvents(): Flow<BillingListenerEvent> {
        return billingEventSubject.filterNotNull()
    }


    /**
     * onUserDataResponse(UserDataResponse userDataResponse): Invoked after a call to getUserData.
     * Determines the UserId and marketplace of the currently logged on user.
     */
    override fun onUserDataResponse(response: UserDataResponse?) {
        _billingEventSubject.value = BillingListenerEvent.UserDataEvent(NowUserData(response!!.userData.userId))
    }

    /**
     * onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse): Invoked after a
     * call to getPurchaseUpdates(boolean reset). Retrieves the purchase history.
     * Amazon recommends that you persist the returned PurchaseUpdatesResponse data
     * and query the system only for updates.
     */
    override fun onProductDataResponse(response: ProductDataResponse) {

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
                _billingEventSubject.value = BillingListenerEvent.ProductDataEvent(NowProductWrapper(productsMap))
            }
            ProductDataResponse.RequestStatus.FAILED -> {

            }
            ProductDataResponse.RequestStatus.NOT_SUPPORTED -> {

            }
        }
    }


    /**
     * onProductDataResponse(ProductDataResponse productDataResponse): Invoked after a call to
     * getProductDataRequest(java.util.Set skus). Retrieves information about SKUs you would like
     * to sell from your app. Use the valid SKUs in onPurchaseResponse.
     */
    override fun onPurchaseResponse(response: PurchaseResponse) {
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

    }
}