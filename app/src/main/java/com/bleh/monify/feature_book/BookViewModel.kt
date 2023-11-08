package com.bleh.monify.feature_book

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class TransferWallet(
    val icon: Int,
    val name: String,
    val nominal: String,
)

data class BookState(
    val searchState: String = "",
    val note: String = "",
    val nominal: String = "",
    val pickedDate: LocalDate = LocalDate.now(),
    val formattedDate: String = "",
    val showDatePicker: Boolean = false,
    val wallet: String = "",
    val isWalletExpanded: Boolean = false,
    val transactionType: Int = 1,
    val selectedCategory: Int = 1,
    val walletSource: TransferWallet? = null,
    val walletDestination: TransferWallet? = null,
    val isWalletSourceExpanded: Boolean = false,
    val isWalletDestinationExpanded: Boolean = false,
)

@HiltViewModel
class BookViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    fun updateSearchState (search: String) {
        _state.update {
            it.copy(searchState = search)
        }
    }

    fun updateNoteState (note: String) {
        _state.update {
            it.copy(note = note)
        }
    }

    fun updateNominalState (nominal: String) {
        _state.update {
            it.copy(nominal = nominal)
        }
    }

    fun updatePickedDateState (date: LocalDate) {
        _state.update {
            it.copy(pickedDate = date)
        }
    }

    fun updateFormattedDate (date: LocalDate) {
        val formattedDate = DateTimeFormatter
            .ofPattern("dd/MM/yyyy")
            .format(date)
        _state.update {
            it.copy(formattedDate = formattedDate)
        }
    }

    fun updateShowDatePicker (show: Boolean) {
        _state.update {
            it.copy(showDatePicker = show)
        }
    }

    fun updateWalletState (wallet: String) {
        _state.update {
            it.copy(wallet = wallet)
        }
    }

    fun updateTransactionType (index: Int) {
        _state.update {
            it.copy(transactionType = index)
        }
    }

    fun updateSelectedCategory (index: Int) {
        _state.update {
            it.copy(selectedCategory = index)
        }
    }

    fun updateWalletExpanded (isExpanded: Boolean) {
        _state.update {
            it.copy(isWalletExpanded = isExpanded)
        }
    }

    fun updateWalletSource (wallet: TransferWallet) {
        _state.update {
            it.copy(walletSource = wallet)
        }
    }

    fun updateWalletDestination (wallet: TransferWallet) {
        _state.update {
            it.copy(walletDestination = wallet)
        }
    }

    fun updateWalletSourceExpanded (isExpanded: Boolean) {
        _state.update {
            it.copy(isWalletSourceExpanded = isExpanded)
        }
    }

    fun updateWalletDestinationExpanded (isExpanded: Boolean) {
        _state.update {
            it.copy(isWalletDestinationExpanded = isExpanded)
        }
    }

    fun resetInputState() {
        _state.update {
            it.copy(
                searchState = "",
                note = "",
                nominal = "",
                pickedDate = LocalDate.now(),
                formattedDate = "",
                showDatePicker = false,
                wallet = "",
                isWalletExpanded = false,
                selectedCategory = 0,
                walletSource = null,
                walletDestination = null,
                isWalletSourceExpanded = false,
                isWalletDestinationExpanded = false,
            )
        }
    }
}