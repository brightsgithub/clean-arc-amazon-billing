package com.example.amazonbillingexample.models

import com.example.domain.models.NowProductWrapper
import com.example.domain.models.NowSku

sealed class PurchaseViewState {

    data class GetProductSkusSuccess(val skus: List<NowSku>) : PurchaseViewState()
    object GetProductSkusFailure : PurchaseViewState()

    data class ProductsLoadedSuccess(val nowProductWrapper: NowProductWrapper) : PurchaseViewState()
    object ProductsLoadedFailure : PurchaseViewState()

    object ShowLoading : PurchaseViewState()
    object HideLoading : PurchaseViewState()
}
