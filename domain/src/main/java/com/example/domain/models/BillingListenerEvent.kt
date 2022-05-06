package com.example.domain.models

sealed class BillingListenerEvent {
    data class UserDataEvent(val data: NowUserData) : BillingListenerEvent()
    data class ProductDataEvent(val data: NowProductWrapper) : BillingListenerEvent()
    object PurchaseSuccessEvent : BillingListenerEvent()
    object AlreadyPurchasedEvent : BillingListenerEvent()
    object FailedToPurchaseEvent : BillingListenerEvent()
}
