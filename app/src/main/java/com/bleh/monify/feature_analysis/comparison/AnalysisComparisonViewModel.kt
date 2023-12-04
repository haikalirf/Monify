package com.bleh.monify.feature_analysis.comparison

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.enums.CategoryType
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

data class AnalysisComparisonState(
    val currentMonth: LocalDate = LocalDate.now(),
    val currentIncome: Double = 0.0,
    val currentOutcome: Double = 0.0,
)

@HiltViewModel
class AnalysisComparisonViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val transactionDao: TransactionDao
): ViewModel() {
    private val _state = MutableStateFlow(AnalysisComparisonState())
    val state = _state.asStateFlow()

    init {
        getIncomes()
        getOutcomes()
    }

    private fun getIncomes() {
        val endDate = _state.value.currentMonth
        val startDate = endDate.minusDays(30)
        viewModelScope.launch {
            transactionDao.sumOfPositiveInRange(startDate, endDate).flowOn(Dispatchers.IO).collect { currentIncome ->
                _state.update {
                    it.copy(currentIncome = currentIncome)
                }
            }
        }
    }

    private fun getOutcomes() {
        val endDate = _state.value.currentMonth
        val startDate = endDate.minusDays(30)
        viewModelScope.launch {
            transactionDao.sumOfNegativeInRange(startDate, endDate).flowOn(Dispatchers.IO).collect { currentOutcome ->
                _state.update {
                    it.copy(currentOutcome = currentOutcome)
                }
            }
        }
    }

    fun nextMonth() {
        _state.update {
            it.copy(currentMonth = it.currentMonth.plusMonths(1))
        }
        getIncomes()
        getOutcomes()
    }

    fun prevMonth() {
        _state.update {
            it.copy(currentMonth = it.currentMonth.minusMonths(1))
        }
        getIncomes()
        getOutcomes()
    }
}