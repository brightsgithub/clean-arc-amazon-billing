package com.example.amazonbillingexample.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amazonbillingexample.models.PurchaseViewState
import com.example.domain.exceptions.NoSavedSkusFoundException
import com.example.domain.models.BillingListenerEvent
import com.example.domain.models.NowSku
import com.example.domain.usecase.interfaces.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException

class BillingViewModel (
    private val purchaseUseCase: PurchaseUseCase,
    private val initBillingUseCase: InitBillingUseCase,
    private val billingListenerUseCaseImpl: BillingListenerUseCase,
    private val getSavedSkusUseCase: GetSavedSkusUseCase,
    private val loadProductDataUseCase: LoadProductDataUseCase,
    private val dispatcherBackground: CoroutineDispatcher,
    private val dispatcherMain: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableSharedFlow<PurchaseViewState>() // for emitting
    private val viewState: Flow<PurchaseViewState> = _state // for clients to listen to

    fun listenForViewUpdates() : Flow<PurchaseViewState> {
        return viewState
    }

    fun initBilling(scope: CoroutineScope = viewModelScope) {
        initBillingUseCase.invoke()
        startListeningForBillingEvents()
    }

    private fun startListeningForBillingEvents(scope: CoroutineScope = viewModelScope) {
        scope.launch  {
            billingListenerUseCaseImpl.invoke()
                .collect {
                    when(it) {
                        is BillingListenerEvent.PurchaseSuccessEvent -> { log("PurchaseSuccessEvent") }
                        is BillingListenerEvent.AlreadyPurchasedEvent -> { log("AlreadyPurchasedEvent") }
                        is BillingListenerEvent.FailedToPurchaseEvent -> { log("FailedToPurchaseEvent") }
                        is BillingListenerEvent.GetProductDataEventSuccess -> {
                            log("GetProductDataEventSuccess")
                            hideLoadingState()
                            _state.emit(PurchaseViewState.ProductsLoadedSuccess(it.data))
                        }
                        is BillingListenerEvent.GetProductDataEventFailure -> {
                            log("GetProductDataEventFailure")
                            hideLoadingState()
                            _state.emit(PurchaseViewState.ProductsLoadedFailure)
                        }
                        is BillingListenerEvent.GetUserDataEventSuccess -> { log("GetUserDataEventSuccess") }
                        is BillingListenerEvent.GetUserDataEventFailure -> { log("GetUserDataEventFailure") }
                    }
                }
        }
    }

    fun getSavedSkus(scope: CoroutineScope = viewModelScope) {
        scope.launch(dispatcherBackground) {
            try {
                showLoadingState()
                val skus = getSavedSkusUseCase.invoke()
                _state.emit(PurchaseViewState.GetProductSkusSuccess(skus))
                hideLoadingState()
            } catch (e: NoSavedSkusFoundException) {
                hideLoadingState()
                e.printStackTrace()
                _state.emit(PurchaseViewState.GetProductSkusFailure)
            }
        }
    }

    fun loadProducts(scope: CoroutineScope = viewModelScope) {
        scope.launch {
        showLoadingState()
            loadProductDataUseCase.invoke()
        }
    }

    fun purchase(sku: String, scope: CoroutineScope = viewModelScope) {
        scope.launch {
        showLoadingState()
            purchaseUseCase.invoke(PurchaseUseCase.Params(sku))
        }
    }

    private fun log(msg: String) {
        Log.v("BillingViewModel", msg)
    }

    private suspend fun showLoadingState() {
        withContext(dispatcherMain) {
            _state.emit(PurchaseViewState.ShowLoading)
        }
    }

    private suspend fun hideLoadingState() {
        withContext(dispatcherMain) {
            _state.emit(PurchaseViewState.HideLoading)
        }
    }

    // Terrible method, but excused as this is for demo purposes
    fun searchForSKU(skus: List<NowSku>, skuToFind: String): String {
        return skus.find { sku -> skuToFind.equals(sku.simpleName) }!!.skuId
    }
}