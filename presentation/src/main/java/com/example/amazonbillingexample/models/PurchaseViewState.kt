package com.example.amazonbillingexample.models

sealed class PurchaseViewState {
    object Success : PurchaseViewState()
    object Failed : PurchaseViewState()
}
