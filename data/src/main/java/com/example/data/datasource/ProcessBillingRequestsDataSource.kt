package com.example.data.datasource

interface ProcessBillingRequestsDataSource {

    fun getUserData()
    fun getPurchaseInfo()
    fun getProductData(skus: Set<String>)
    fun purchaseItem(sku:String)
}