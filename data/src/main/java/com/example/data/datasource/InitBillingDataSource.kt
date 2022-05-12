package com.example.data.datasource

import kotlinx.coroutines.CoroutineScope

interface InitBillingDataSource {
    fun initBilling(coroutineScope: CoroutineScope): Boolean
}