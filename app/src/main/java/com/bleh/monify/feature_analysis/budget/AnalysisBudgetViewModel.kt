package com.bleh.monify.feature_analysis.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.core.daos.BudgetDao
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.enums.BudgetType
import com.bleh.monify.core.pojos.BudgetCategory
import com.bleh.monify.feature_auth.GoogleAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalysisBudgetState(
    val budgetTimeFrame: BudgetType = BudgetType.MONTHLY,
    val isDropDownExpanded: Boolean = false,
    val budgetList: List<BudgetCategory> = listOf()
)

@HiltViewModel
class AnalysisBudgetViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val budgetDao: BudgetDao
): ViewModel() {
    private val _state = MutableStateFlow(AnalysisBudgetState())
    val state = _state.asStateFlow()

    init {
        getBudgets()
    }

    private fun getBudgets() {
        viewModelScope.launch {
            budgetDao.getBudgetsWithCategory().flowOn(Dispatchers.IO).collect { budgetList ->
                _state.update {
                    it.copy(budgetList = budgetList)
                }
            }
        }
    }

    fun updateBudgetTimeFrame (timeFrame: BudgetType) {
        _state.update {
            it.copy(budgetTimeFrame = timeFrame)
        }
    }

    fun updateDropDownExpanded (isExpanded: Boolean) {
        _state.update {
            it.copy(isDropDownExpanded = isExpanded)
        }
    }
}