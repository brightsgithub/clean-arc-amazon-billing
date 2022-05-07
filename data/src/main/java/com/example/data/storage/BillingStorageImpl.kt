package com.example.data.storage

import com.example.domain.models.NowSku
import com.example.domain.models.NowUserData

class BillingStorageImpl(private val appPreferences: AppPreferences) : BillingStorage {

    override suspend fun saveUserData(nowUserData: NowUserData) {
        TODO("Not yet implemented")
    }

    override suspend fun getSavedProductSkus(): List<NowSku> {
        // We should be getting this from our backend systems
        val CONSUMEABLE_APPLE_SKU = "com.example.amazonbillingexampl.apple"
        val CONSUMEABLE_MANGO_SKU = "com.example.amazonbillingexampl.mango"

        val skus = arrayListOf<NowSku>()
        skus.add(NowSku(CONSUMEABLE_APPLE_SKU))
        skus.add(NowSku(CONSUMEABLE_MANGO_SKU))
        return skus
    }

    override suspend fun saveProductSku(sku: NowSku) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSavedProductSku(sku: NowSku) {
        TODO("Not yet implemented")
    }
}