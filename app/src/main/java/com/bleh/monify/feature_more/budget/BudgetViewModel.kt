package com.bleh.monify.feature_more.budget

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import com.bleh.monify.core.enums.BudgetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject



data class BudgetState(
    val budgetType: BudgetType = BudgetType.MONTHLY,
    val budgetName: String = "",
    val budgetValue: String = "",
)

@HiltViewModel
class BudgetViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(BudgetState())
    val state = _state.asStateFlow()

    fun updateBudgetType (type: BudgetType) {
        _state.update {
            it.copy(budgetType = type)
        }
    }

    fun updateBudgetName (name: String) {
        _state.update {
            it.copy(budgetName = name)
        }
    }

    fun updateBudgetValue (value: String) {
        _state.update {
            it.copy(budgetValue = value)
        }
    }

    fun resetInputState() {

    }
}