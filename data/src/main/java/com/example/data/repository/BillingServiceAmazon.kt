package com.example.data.repository

import com.amazon.device.iap.PurchasingListener
import com.amazon.device.iap.PurchasingService
import com.amazon.device.iap.model.ProductDataResponse
import com.amazon.device.iap.model.PurchaseResponse
import com.amazon.device.iap.model.PurchaseUpdatesResponse
import com.amazon.device.iap.model.UserDataResponse

class BillingServiceAmazon : PurchasingListener, BillingService {

    private var listener: BillingService.Listener? = null

    override fun setListener(listener: BillingService.Listener) {
        this.listener = listener
    }

    override fun purchaseItem(sku: String): String {
        return PurchasingService.purchase(sku).toString()
    }

    override fun onUserDataResponse(p0: UserDataResponse?) {
        TODO("Not yet implemented")
    }

    override fun onProductDataResponse(p0: ProductDataResponse?) {
        TODO("Not yet implemented")
    }

    override fun onPurchaseResponse(p0: PurchaseResponse?) {
        listener?.onBillingPurchaseResponse(
            BillingService.BillingPurchaseResponse(
                p0?.requestId.toString(),
                status = convertStatus(p0?.requestStatus ?: PurchaseResponse.RequestStatus.FAILED)
            )
        )
    }

    private fun convertStatus(requestStatus: PurchaseResponse.RequestStatus): BillingService.Status {
        return when (requestStatus) {
            PurchaseResponse.RequestStatus.SUCCESSFUL -> BillingService.Status.SUCCESSFUL
            PurchaseResponse.RequestStatus.FAILED -> BillingService.Status.FAILED
            PurchaseResponse.RequestStatus.ALREADY_PURCHASED -> BillingService.Status.ALREADY_PURCHASED
            PurchaseResponse.RequestStatus.INVALID_SKU -> BillingService.Status.INVALID_SKU
            PurchaseResponse.RequestStatus.NOT_SUPPORTED -> BillingService.Status.NOT_SUPPORTED
        }
    }

    override fun onPurchaseUpdatesResponse(p0: PurchaseUpdatesResponse?) {
        TODO("Not yet implemented")
    }
}