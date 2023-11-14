package com.bleh.monify.feature_wallet

import androidx.lifecycle.ViewModel
import com.bleh.monify.feature_book.BookState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class WalletState(
    val totalNominal: Double = 0.0,
    val walletName: String = "",
    val walletNominal: String = "",
    val selectedWallet: Int = 0,
)

@HiltViewModel
class WalletViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(WalletState())
    val state = _state.asStateFlow()

    fun updateWalletNameState (name: String) {
        _state.update {
            it.copy(walletName = name)
        }
    }

    fun updateWalletNominalState (nominal: String) {
        _state.update {
            it.copy(walletNominal = nominal)
        }
    }

    fun updateSelectedWalletState (selectedWallet: Int) {
        _state.update {
            it.copy(selectedWallet = selectedWallet)
        }
    }
}
