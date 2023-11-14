package com.bleh.monify.feature_analysis.comparison

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AnalysisComparisonState(
    val currentDate: String = "2023-11",
    val income: Double = 1000000.0,
    val outcome: Double = 100000.0,
)

@HiltViewModel
class AnalysisComparisonViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AnalysisComparisonState())
    val state = _state.asStateFlow()

    fun updateCurrentDate (date: String) {
        _state.update {
            it.copy(currentDate = date)
        }
    }

    fun updateIncome (income: Double) {
        _state.update {
            it.copy(income = income)
        }
    }

    fun updateOutcome (outcome: Double) {
        _state.update {
            it.copy(outcome = outcome)
        }
    }
}