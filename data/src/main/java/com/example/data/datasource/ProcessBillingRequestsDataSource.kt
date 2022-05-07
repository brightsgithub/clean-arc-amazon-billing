package com.example.data.datasource

import com.example.domain.models.NowSku

interface ProcessBillingRequestsDataSource {

    fun getUserData()
    fun getPurchaseInfo()
    fun getProductData(skus: List<NowSku>)
    fun purchaseItem(sku:String)
}