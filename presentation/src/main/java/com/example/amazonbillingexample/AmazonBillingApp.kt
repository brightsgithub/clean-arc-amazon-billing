package com.example.amazonbillingexample

import android.app.Application
import com.example.amazonbillingexample.viewmodels.AmazonBillingViewModel
import com.example.data.datasource.amazon.AmazonPurchasingListenerDataSource
import com.example.data.datasource.BillingListenerDataSource
import com.example.data.repository.BillingRepositoryImpl
import com.example.domain.repository.BillingRepository
import com.example.domain.usecase.PurchaseUseCase
import com.example.domain.usecase.PurchaseUseCaseImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

open class AmazonBillingApp: Application() {

    companion object {
        lateinit var instance: AmazonBillingApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@AmazonBillingApp)
            modules(applicationModule)
        }
    }

    var applicationModule: Module = module {

        single<BillingListenerDataSource> { AmazonPurchasingListenerDataSource() }
        single<BillingRepository> {
            BillingRepositoryImpl(billingListenerDataSource = get(), initBillingDataSource = get())
        }
        factory<PurchaseUseCase> { PurchaseUseCaseImpl() }

        viewModel {
            AmazonBillingViewModel(get())
        }
    }
}