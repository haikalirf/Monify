package com.bleh.monify.feature_analysis.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.core.pojos.CategoryWithSumAndPercentage
import com.bleh.monify.feature_analysis.analysis.AnalysisType
import com.bleh.monify.feature_auth.GoogleAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AnalysisTransactionState(
    val analysisType: AnalysisType = AnalysisType.OUTCOME,
    val currentMonth: LocalDate = LocalDate.now(),
    val incomeList: List<CategoryWithSumAndPercentage> = listOf(),
    val incomeSum: Double = 0.0,
    val outcomeList: List<CategoryWithSumAndPercentage> = listOf(),
    val outcomeSum: Double = 0.0
)

@HiltViewModel
class AnalysisTransactionViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val transactionDao: TransactionDao,
): ViewModel() {
    private val _state = MutableStateFlow(AnalysisTransactionState())
    val state = _state.asStateFlow()

    private val currentUser = googleAuthClient.getLoggedInUser()

    init {
        getIncomes()
        getIncomeSum()
        getOutcomes()
        getOutcomeSum()
    }

    private fun getIncomes() {
        val endDate = _state.value.currentMonth
        val startDate = endDate.minusDays(30)
        viewModelScope.launch {
            transactionDao.sumOfCategoriesInRangeAndType(startDate, endDate, CategoryType.INCOME, currentUser!!.userId).flowOn(Dispatchers.IO).collect { incomeList ->
                _state.update {
                    it.copy(incomeList = incomeList)
                }
            }
        }
    }

    private fun getIncomeSum() {
        val endDate = _state.value.currentMonth
        val startDate = endDate.minusDays(30)
        viewModelScope.launch {
            transactionDao.sumOfPositiveInRange(startDate, endDate, currentUser!!.userId).flowOn(Dispatchers.IO).collect { incomeSum ->
                _state.update {
                    it.copy(incomeSum = incomeSum)
                }
            }
        }
    }

    private fun getOutcomes() {
        val endDate = _state.value.currentMonth
        val startDate = endDate.minusDays(30)
        viewModelScope.launch {
            transactionDao.sumOfCategoriesInRangeAndType(startDate, endDate, CategoryType.OUTCOME, currentUser!!.userId).flowOn(Dispatchers.IO).collect { outcomeList ->
                _state.update {
                    it.copy(outcomeList = outcomeList)
                }
            }
        }
    }

    private fun getOutcomeSum() {
        val endDate = _state.value.currentMonth
        val startDate = endDate.minusDays(30)
        viewModelScope.launch {
            transactionDao.sumOfNegativeInRange(startDate, endDate, currentUser!!.userId).flowOn(Dispatchers.IO).collect { outcomeSum ->
                _state.update {
                    it.copy(outcomeSum = outcomeSum)
                }
            }
        }
    }

    fun previousMonth() {
        _state.update {
            it.copy(currentMonth = it.currentMonth.minusMonths(1))
        }
        getIncomes()
        getIncomeSum()
        getOutcomes()
        getOutcomeSum()
    }

    fun nextMonth() {
        _state.update {
            it.copy(currentMonth = it.currentMonth.plusMonths(1))
        }
        getIncomes()
        getIncomeSum()
        getOutcomes()
        getOutcomeSum()
    }

    fun updateAnalysisType (type: AnalysisType) {
        _state.update {
            it.copy(analysisType = type)
        }
    }
}