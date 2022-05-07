package com.example.data.storage

import com.example.domain.models.NowSku
import com.example.domain.models.NowUserData

/**
 * This is where we will store In App Billing related info.
 * Could be in memory, db, preferences external server
 */
interface BillingStorage {

    // User related info
    suspend fun saveUserData(nowUserData: NowUserData)

    // Sku related info
    fun getSavedProductSkus() : List<NowSku>
    suspend fun saveProductSku(sku: NowSku)
    suspend fun deleteSavedProductSku(sku: NowSku)
}