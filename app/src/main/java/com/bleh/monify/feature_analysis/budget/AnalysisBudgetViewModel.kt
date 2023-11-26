package com.bleh.monify.feature_analysis.budget

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import com.bleh.monify.core.enums.BudgetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class UiBudget(
    val icon: Int,
    val name: String,
    val amount: Double,
    val used: Double,
    val budgetType: BudgetType
)

data class AnalysisBudgetState(
    val budgetTimeFrame: BudgetType = BudgetType.MONTHLY,
    val isDropDownExpanded: Boolean = false,
    val uiBudgetLists: List<UiBudget> = listOf(
        UiBudget(
            icon = R.drawable.ic_food,
            name = "Manakan & Minuman",
            amount = 800000.0,
            used = 800000.0,
            budgetType = BudgetType.MONTHLY
        ),
        UiBudget(
            icon = R.drawable.ic_health,
            name = "Kesehatan",
            amount = 200000.0,
            used = 400000.0,
            budgetType = BudgetType.MONTHLY
        ),
        UiBudget(
            icon = R.drawable.ic_education,
            name = "Pendidikan",
            amount = 10000000.0,
            used = 1000000.0,
            budgetType = BudgetType.MONTHLY
        ),
    )
)

@HiltViewModel
class AnalysisBudgetViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AnalysisBudgetState())
    val state = _state.asStateFlow()

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

    fun updateBudgetList (list: List<UiBudget>) {
        _state.update {
            it.copy(uiBudgetLists = list)
        }
    }
}