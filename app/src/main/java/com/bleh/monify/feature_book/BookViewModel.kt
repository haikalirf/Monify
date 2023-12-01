package com.bleh.monify.feature_book

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.entities.TransactionEntity
import com.bleh.monify.core.entities.Wallet
import com.bleh.monify.core.pojos.TransactionCategoryWallet
import com.bleh.monify.feature_auth.GoogleAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class BookState(
    val searchState: String = "",
    val note: String = "",
    val nominal: String = "",
    val pickedDate: LocalDate = LocalDate.now(),
    val formattedDate: String = "",
    val showDatePicker: Boolean = false,
    val isWalletExpanded: Boolean = false,
    val admin: String = "",
    val transactionType: Int = 1,
    val selectedCategory: Int? = null,
    val selectedCategoryId: Int? = null,
    val walletSource: Wallet? = null,
    val walletSourceId: Int = 0,
    val walletDestination: Wallet? = null,
    val walletDestinationId: Int? = null,
    val isWalletSourceExpanded: Boolean = false,
    val isWalletDestinationExpanded: Boolean = false,
    val positiveSum: Double = 0.0,
    val negativeSum: Double = 0.0,
    val currentTransactionId: Int = 0,
    val isEdit: Boolean = false,
    val transactionList: List<TransactionCategoryWallet> = listOf(),
    val categoryList: List<Category> = listOf(),
    val walletList: List<Wallet> = listOf()
)

@HiltViewModel
class BookViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val walletDao: WalletDao,
): ViewModel() {
    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    private val currentUser = googleAuthClient.getLoggedInUser()

    init {
        getTransactions()
        getCategories()
        getWallets()
        getPositiveSum()
        getNegativeSum()
    }

    private fun getTransactions() {
        viewModelScope.launch {
            transactionDao.getTransactionCategoryWallets().flowOn(Dispatchers.IO).collect { transactionList ->
                _state.update {
                    it.copy(transactionList = transactionList)
                }
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            categoryDao.getCategories().flowOn(Dispatchers.IO).collect { categoryList ->
                _state.update {
                    it.copy(categoryList = categoryList)
                }
            }
        }
    }

    private fun getWallets() {
        viewModelScope.launch {
            walletDao.getWallets().flowOn(Dispatchers.IO).collect { walletList ->
                _state.update {
                    it.copy(walletList = walletList)
                }
            }
        }
    }

    private fun getPositiveSum() {
        viewModelScope.launch {
            val endDate = LocalDate.now()
            val startDate = endDate.minusDays(30)
            transactionDao.sumOfPositiveInRange(startDate, endDate).flowOn(Dispatchers.IO).collect { sum ->
                _state.update {
                    it.copy(positiveSum = sum)
                }
            }
        }
    }

    private fun getNegativeSum() {
        viewModelScope.launch {
            val endDate = LocalDate.now()
            val startDate = endDate.minusDays(30)
            transactionDao.sumOfNegativeInRange(startDate, endDate).flowOn(Dispatchers.IO).collect { sum ->
                _state.update {
                    it.copy(negativeSum = sum)
                }
            }
        }
    }

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

    fun updateTransactionType (index: Int) {
        _state.update {
            it.copy(transactionType = index)
        }
    }

    fun updateSelectedCategory (index: Int?) {
        _state.update {
            it.copy(selectedCategory = index)
        }
    }

    fun updateSelectedCategoryId (id: Int) {
        _state.update {
            it.copy(selectedCategoryId = id)
        }
    }

    fun updateWalletExpanded (isExpanded: Boolean) {
        _state.update {
            it.copy(isWalletExpanded = isExpanded)
        }
    }

    fun updateAdminState (admin: String) {
        _state.update {
            it.copy(admin = admin)
        }
    }

    fun updateWalletSource (wallet: Wallet) {
        _state.update {
            it.copy(walletSource = wallet)
        }
    }

    fun updateWalletSourceId (id: Int) {
        _state.update {
            it.copy(walletSourceId = id)
        }
    }

    fun updateWalletDestination (wallet: Wallet?) {
        _state.update {
            it.copy(walletDestination = wallet)
        }
    }

    fun updateWalletDestinationId (id: Int) {
        _state.update {
            it.copy(walletDestinationId = id)
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

    fun updateCurrentTransactionId (id: Int) {
        _state.update {
            it.copy(currentTransactionId = id)
        }
    }

    fun updateIsEditState (isEdit: Boolean) {
        _state.update {
            it.copy(isEdit = isEdit)
        }
    }

    fun upsertTransaction(context: Context): Exception? {
        var err: Exception? = null
        viewModelScope.launch {
            try {
//                Log.d("BookViewModel", "upsertTransaction: ${currentUser!!.userId}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.walletSourceId}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.walletDestinationId}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.selectedCategoryId}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.transactionType == 2}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.note}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.nominal.toDouble()}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.admin.toDoubleOrNull()}")
//                Log.d("BookViewModel", "upsertTransaction: ${state.value.pickedDate}")
                if (state.value.transactionType != 2 && state.value.selectedCategoryId == null) {
                    throw Exception("Category must be selected")
                }
                val balance = state.value.nominal.toDouble() * if (state.value.transactionType == 1) -1 else 1
                transactionDao.upsertTransaction(
                    TransactionEntity(
                        id = state.value.currentTransactionId,
                        userId = currentUser!!.userId,
                        walletFromId = state.value.walletSourceId,
                        walletToId = state.value.walletDestinationId,
                        categoryId = state.value.selectedCategoryId,
                        isTransfer = state.value.transactionType == 2,
                        description = state.value.note,
                        balance = balance,
                        admin = state.value.admin.toDoubleOrNull(),
                        date = state.value.pickedDate
                    )
                )
                Toast.makeText(context, "Transaction Saved", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("BookViewModel", "upsertTransaction: $e")
                Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show()
                err = e
            }
        }
        return err
    }

    fun deleteTransaction(context: Context, id: Int): Exception? {
        var err: Exception? = null
        viewModelScope.launch {
            try {
                transactionDao.deleteTransaction(id)
                Toast.makeText(context, "Transaction Deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("BookViewModel", "deleteTransaction: $e")
                Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show()
                err = e
            }
        }
        return err
    }

    fun resetInputState() {
        Log.d("BookViewModel", "resetInputState called")
        _state.update {
            it.copy(
                searchState = "",
                note = "",
                nominal = "",
                pickedDate = LocalDate.now(),
                formattedDate = "",
                showDatePicker = false,
                isWalletExpanded = false,
                admin = "",
                transactionType = 1,
                selectedCategory = null,
                selectedCategoryId = null,
                walletSource = null,
                walletSourceId = 0,
                walletDestination = null,
                walletDestinationId = null,
                isWalletSourceExpanded = false,
                isWalletDestinationExpanded = false,
                currentTransactionId = 0,
                isEdit = false
            )
        }
    }

    fun logAllStates() {
        Log.d("BookViewModel", "logAllStates: (search) ${state.value.searchState}")
        Log.d("BookViewModel", "logAllStates: (note) ${state.value.note}")
        Log.d("BookViewModel", "logAllStates: (nominal) ${state.value.nominal}")
        Log.d("BookViewModel", "logAllStates: (pickedDate) ${state.value.pickedDate}")
        Log.d("BookViewModel", "logAllStates: (formattedDate) ${state.value.formattedDate}")
        Log.d("BookViewModel", "logAllStates: (showDatePicker) ${state.value.showDatePicker}")
        Log.d("BookViewModel", "logAllStates: (isWalletExpanded) ${state.value.isWalletExpanded}")
        Log.d("BookViewModel", "logAllStates: (admin) ${state.value.admin}")
        Log.d("BookViewModel", "logAllStates: (transactionType) ${state.value.transactionType}")
        Log.d("BookViewModel", "logAllStates: (selectedCategory) ${state.value.selectedCategory}")
        Log.d("BookViewModel", "logAllStates: (selectedCategoryId) ${state.value.selectedCategoryId}")
        Log.d("BookViewModel", "logAllStates: (walletSource) ${state.value.walletSource}")
        Log.d("BookViewModel", "logAllStates: (walletSourceId) ${state.value.walletSourceId}")
        Log.d("BookViewModel", "logAllStates: (walletDestination) ${state.value.walletDestination}")
        Log.d("BookViewModel", "logAllStates: (walletDestinationId) ${state.value.walletDestinationId}")
        Log.d("BookViewModel", "logAllStates: (isWalletSourceExpanded) ${state.value.isWalletSourceExpanded}")
        Log.d("BookViewModel", "logAllStates: (isWalletDestinationExpanded) ${state.value.isWalletDestinationExpanded}")
        Log.d("BookViewModel", "logAllStates: (positiveSum) ${state.value.positiveSum}")
        Log.d("BookViewModel", "logAllStates: (negativeSum) ${state.value.negativeSum}")
        Log.d("BookViewModel", "logAllStates: (currentTransactionId) ${state.value.currentTransactionId}")
        Log.d("BookViewModel", "logAllStates: (isEdit) ${state.value.isEdit}")
    }
}