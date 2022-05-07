package com.example.amazonbillingexample.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amazonbillingexample.models.PurchaseViewState
import com.example.domain.models.BillingListenerEvent
import com.example.domain.usecase.BillingListenerUseCase
import com.example.domain.usecase.BillingListenerUseCaseImpl
import com.example.domain.usecase.InitBillingUseCase
import com.example.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

class BillingViewModel (
    private val purchaseUseCase: PurchaseUseCase,
    private val initBillingUseCase: InitBillingUseCase,
    private val billingListenerUseCaseImpl: BillingListenerUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow<PurchaseViewState?>(null)
    private val viewState: Flow<PurchaseViewState?> = _state

    fun listenForViewUpdates() : Flow<PurchaseViewState?> {
        return viewState
    }

    fun initBilling() {
        initBillingUseCase.invoke()
    }

    fun purchase(sku: String, scope: CoroutineScope = viewModelScope) {
        // You will need different coroutines, since collect() is a suspending function that
        // suspends until your Flow terminates. To avoid suspending this entire block,
        // we launch child jobs. This will be killed when the parent scope is killed
        // https://stackoverflow.com/questions/67799859/collect-from-several-stateflows


        Log.v("BillingViewModel", "1 " + Thread.currentThread().name)
        scope.launch {
            Log.v("BillingViewModel", "2 " + Thread.currentThread().name)
            launch  {
                Log.v("BillingViewModel", "billingListenerUseCaseImpl START!!!!" + Thread.currentThread().name)
                billingListenerUseCaseImpl.invoke()
                    .collect {
                        when(it) {
                            is BillingListenerEvent.PurchaseSuccessEvent -> {
                                Log.v("BillingViewModel", "PurchaseSuccessEvent")
                            }
                            is BillingListenerEvent.AlreadyPurchasedEvent -> {
                                Log.v("BillingViewModel", "AlreadyPurchasedEvent")
                            }
                            is BillingListenerEvent.FailedToPurchaseEvent -> {
                                Log.v("BillingViewModel", "FailedToPurchaseEvent")
                            }
                            is BillingListenerEvent.ProductDataEvent -> {
                                Log.v("BillingViewModel", "ProductDataEvent")
                            }
                            is BillingListenerEvent.UserDataEvent -> {
                                Log.v("BillingViewModel", "UserDataEvent")
                            }
                        }
                    }
                Log.v("BillingViewModel", "billingListenerUseCaseImpl FINISHED!!!!")
            }

            launch {
                Log.v("BillingViewModel", "purchaseUseCase START!!!!" + Thread.currentThread().name)
                delay(2000)
                purchaseUseCase.invoke(PurchaseUseCase.Params(sku))
                Log.v("BillingViewModel", "purchaseUseCase FINISHED!!!!" + Thread.currentThread().name)
            }

            Thread(Runnable {
                Thread.sleep(5000)
                GlobalScope.launch(dispatcherBackground) {

                    Log.v("BillingViewModel", "inside the new thread? "+ Thread.currentThread().name)
                    _state.value = PurchaseViewState.Success
                }
            }).start()

        }

        Log.v("BillingViewModel", "METHOD FINISHED!!!!")

    }
}