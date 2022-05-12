package com.example.amazonbillingexample

import android.app.Application
import com.example.amazonbillingexample.models.PurchaseViewState
import com.example.amazonbillingexample.viewmodels.BillingViewModel
import com.example.data.datasource.amazon.AmazonPurchasingListenerDataSource
import com.example.data.datasource.BillingListenerDataSource
import com.example.data.datasource.InitBillingDataSource
import com.example.data.datasource.ProcessBillingRequestsDataSource
import com.example.data.datasource.amazon.AmazonIapManager
import com.example.data.datasource.amazon.AmazonProcessBillingRequestsDataSource
import com.example.data.datasource.amazon.InitAmazonBillingDataSource
import com.example.data.repository.BillingRepositoryImpl
import com.example.data.storage.AppPreferences
import com.example.data.storage.AppPreferencesImpl
import com.example.data.storage.BillingStorage
import com.example.data.storage.BillingStorageImpl
import com.example.domain.repository.BillingRepository
import com.example.domain.usecase.*
import com.example.domain.usecase.interfaces.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO = "IO"
const val MAIN = "MAIN"

class AmazonBillingApp: Application() {

    private val _state = MutableStateFlow<PurchaseViewState?>(null)
    private val viewState: Flow<PurchaseViewState?> = _state

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

        factory<CoroutineDispatcher>(named(IO)) {
            Dispatchers.IO
        }

        factory<CoroutineDispatcher>(named(MAIN)) {
            Dispatchers.Main
        }


        factory<ProcessBillingRequestsDataSource> { AmazonProcessBillingRequestsDataSource(get())}
        factory<InitBillingDataSource> { InitAmazonBillingDataSource(get(), instance) }
        factory { AmazonIapManager(get()) }
        single<BillingListenerDataSource> { AmazonPurchasingListenerDataSource(get()) } // Must use the same instance!
        single<AppPreferences> { AppPreferencesImpl(instance) }
        single<BillingStorage> { BillingStorageImpl(get()) }

        single<BillingRepository> {
            BillingRepositoryImpl(get(), get(), get(), get())
        }

        factory<PurchaseUseCase> { PurchaseUseCaseImpl(get()) }
        factory<UnRegisterBillingUseCase> { UnRegisterBillingUseCaseImpl(get()) }
        factory<InitBillingUseCase> { InitBillingUseCaseImpl(get()) }
        factory <BillingListenerUseCase> { BillingListenerUseCaseImpl(get()) }
        factory <GetSavedSkusUseCase> { GetSavedSkusUseCaseImpl(get()) }
        factory <LoadProductDataUseCase> { LoadProductDataUseCaseImpl(get()) }

        viewModel {
            BillingViewModel(get(), get(), get(), get(), get(), get(), get(named(IO)), get(named(MAIN)))
        }
    }
}