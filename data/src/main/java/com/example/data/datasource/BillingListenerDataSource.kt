package com.example.data.datasource

import com.amazon.device.iap.PurchasingListener
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowUserData
import kotlinx.coroutines.flow.Flow

interface BillingListenerDataSource: PurchasingListener {

    // here we can decorate with extra interface methods we might need
    fun listenForBillingEvents(): Flow<BillingListenerEvent>
    

}