package com.example.domain.models

sealed class BillingListenerEvent {

    data class GetUserDataEventSuccess(val data: NowUserData) : BillingListenerEvent()
    object GetUserDataEventFailure : BillingListenerEvent()

    data class GetProductDataEventSuccess(val data: NowProductWrapper) : BillingListenerEvent()
    object GetProductDataEventFailure : BillingListenerEvent()

    object PurchaseSuccessEvent : BillingListenerEvent()
    object AlreadyPurchasedEvent : BillingListenerEvent()
    object FailedToPurchaseEvent : BillingListenerEvent()
}
