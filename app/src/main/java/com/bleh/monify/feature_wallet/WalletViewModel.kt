package com.bleh.monify.feature_wallet

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Wallet
import com.bleh.monify.feature_auth.GoogleAuthClient
import com.bleh.monify.feature_wallet.helper.walletIconList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
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
    private val googleAuthClient: GoogleAuthClient,
    private val walletDao: WalletDao
): ViewModel() {
    private val _state = MutableStateFlow(WalletState())
    val state = _state.asStateFlow()

    private val currentUser = googleAuthClient.getLoggedInUser()

    init {
        getWallets()
        getSum()
    }

    private fun getWallets () {
        viewModelScope.launch {
            walletDao.getWallets(currentUser!!.userId).flowOn(Dispatchers.IO).collect { walletList ->
                _state.update {
                    it.copy(wallets = walletList)
                }
            }
        }
    }

    private fun getSum() {
        viewModelScope.launch {
            walletDao.walletSum(currentUser!!.userId).flowOn(Dispatchers.IO).collect { sum ->
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

    fun upsertWallet(context: Context): Exception? {
        var err: Exception? = null
        viewModelScope.launch {
            try {
                walletDao.upsertWallet(
                    Wallet(
                        id = if(state.value.isEdit) state.value.currentEditId else 0,
                        userId = currentUser!!.userId,
                        name = state.value.walletName,
                        balance = state.value.walletNominal.toDouble(),
                        icon = walletIconList()[state.value.selectedWallet]
                    ),
                )
            } catch (e: NumberFormatException) {
                Log.d("WalletViewModel", "upsertWallet: $e")
                Toast.makeText(context, "There is a problem with the input", Toast.LENGTH_LONG).show()
                err = e
            } catch (e: Exception) {
                Log.d("WalletViewModel", "upsertWallet: $e")
                Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show()
                err = e
            }
        }
        return err
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
