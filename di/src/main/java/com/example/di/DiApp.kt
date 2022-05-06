package com.example.di

import com.example.amazonbillingexample.AmazonBillingApp
import com.example.amazonbillingexample.viewmodels.AmazonBillingViewModel
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

class DiApp: AmazonBillingApp() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DiApp)
            modules(applicationModule)
        }
    }

    var applicationModule: Module = module {
        single<BillingRepository> { BillingRepositoryImpl() }
        factory<PurchaseUseCase> { PurchaseUseCaseImpl() }

        viewModel {
            AmazonBillingViewModel(get())
        }
    }
}