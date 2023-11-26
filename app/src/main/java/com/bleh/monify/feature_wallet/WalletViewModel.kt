package com.bleh.monify.feature_wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Wallet
import com.bleh.monify.feature_wallet.helper.walletIconList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletState(
    val totalNominal: Double = 0.0,
    val walletName: String = "",
    val walletNominal: String = "",
    val selectedWallet: Int = 0,
    val wallets: List<Wallet> = listOf(),
    val isEdit: Boolean = false,
    val currentEditId: Int = 0,
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletDao: WalletDao
): ViewModel() {
    private val _state = MutableStateFlow(WalletState())
    val state = _state.asStateFlow()

    init {
        getWallets()
        getSum()
    }

    private fun getWallets () {
        viewModelScope.launch {
            walletDao.getWallets().flowOn(Dispatchers.IO).collect { walletList ->
                _state.update {
                    it.copy(wallets = walletList)
                }
            }
        }
    }

    private fun getSum() {
        viewModelScope.launch {
            walletDao.walletSum().flowOn(Dispatchers.IO).collect { sum ->
                _state.update {
                    it.copy(totalNominal = sum)
                }
            }
        }
    }

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

    fun updateIsEditState (isEdit: Boolean) {
        _state.update {
            it.copy(isEdit = isEdit)
        }
    }

    fun updateCurrentEditIdState (currentEditId: Int) {
        _state.update {
            it.copy(currentEditId = currentEditId)
        }
    }

    fun upsertWallet () {
        viewModelScope.launch {
            walletDao.upsertWallet(
                Wallet(
                    id = if(state.value.isEdit) state.value.currentEditId else 0,
                    userId = 1,
                    name = state.value.walletName,
                    balance = state.value.walletNominal.toDouble(),
                    icon = walletIconList()[state.value.selectedWallet]
                ),
            )
        }
    }

    fun setDeletedTrue (id: Int) {
        viewModelScope.launch {
            walletDao.setDeletedTrue(id)
        }
    }

    fun resetState () {
        _state.update {
            it.copy(
                walletName = "",
                walletNominal = "",
                selectedWallet = 0,
                isEdit = false,
                currentEditId = 0
            )
        }
    }
}
