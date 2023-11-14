package com.bleh.monify.feature_analysis.budget

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class BudgetTimeFrame(val value: String) {
    WEEK("Mingguan"),
    MONTH("Bulanan")
}

data class Budget(
    val icon: Int,
    val name: String,
    val amount: Double,
    val used: Double,
    val timeFrame: BudgetTimeFrame
)

data class AnalysisBudgetState(
    val budgetTimeFrame: BudgetTimeFrame = BudgetTimeFrame.MONTH,
    val isDropDownExpanded: Boolean = false,
    val budgetList: List<Budget> = listOf(
        Budget(
            icon = R.drawable.ic_food,
            name = "Manakan & Minuman",
            amount = 800000.0,
            used = 800000.0,
            timeFrame = BudgetTimeFrame.MONTH
        ),
        Budget(
            icon = R.drawable.ic_health,
            name = "Kesehatan",
            amount = 200000.0,
            used = 400000.0,
            timeFrame = BudgetTimeFrame.MONTH
        ),
        Budget(
            icon = R.drawable.ic_education,
            name = "Pendidikan",
            amount = 10000000.0,
            used = 1000000.0,
            timeFrame = BudgetTimeFrame.MONTH
        ),
    )
)

@HiltViewModel
class AnalysisBudgetViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AnalysisBudgetState())
    val state = _state.asStateFlow()

    fun updateBudgetTimeFrame (timeFrame: BudgetTimeFrame) {
        _state.update {
            it.copy(budgetTimeFrame = timeFrame)
        }
    }

    fun updateDropDownExpanded (isExpanded: Boolean) {
        _state.update {
            it.copy(isDropDownExpanded = isExpanded)
        }
    }

    fun updateBudgetList (list: List<Budget>) {
        _state.update {
            it.copy(budgetList = list)
        }
    }
}