package com.bleh.monify.feature_more.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.R
import com.bleh.monify.core.daos.BudgetDao
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.enums.BudgetType
import com.bleh.monify.core.pojos.BudgetCategory
import com.bleh.monify.feature_auth.GoogleAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetState(
    val budgetType: BudgetType = BudgetType.MONTHLY,
    val showEditDialog: Boolean = false,
    val currentEditId: Int = 0,
    val currentBudgetName: String = "",
    val currentBudgetNominal: Double? = 0.0,
    val budgetList: List<BudgetCategory> = listOf()
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val budgetDao: BudgetDao
): ViewModel() {
    private val _state = MutableStateFlow(BudgetState())
    val state = _state.asStateFlow()

    init {
        getBudget()
    }

    private fun getBudget () {
        viewModelScope.launch {
            budgetDao.getBudgetsWithCategory().collect { budgets ->
                _state.update {
                    it.copy(budgetList = budgets)
                }
            }
        }
    }

    fun updateBudgetType (type: BudgetType) {
        _state.update {
            it.copy(budgetType = type)
        }
    }

    fun updateShowEditDialog (show: Boolean) {
        _state.update {
            it.copy(showEditDialog = show)
        }
    }

    fun updateCurrentEditId (id: Int) {
        _state.update {
            it.copy(currentEditId = id)
        }
    }

    fun updateCurrentBudgetName (name: String) {
        _state.update {
            it.copy(currentBudgetName = name)
        }
    }

    fun updateCurrentBudgetNominal (nominal: Double?) {
        _state.update {
            it.copy(currentBudgetNominal = nominal)
        }
    }

    fun updateWeeklyBudget (id: Int, amount: Double?) {
        viewModelScope.launch {
            budgetDao.updateWeeklyAmount(id, amount)
        }
    }

    fun updateMonthlyBudget (id: Int, amount: Double?) {
        viewModelScope.launch {
            budgetDao.updateMonthlyAmount(id, amount)
        }
    }

    fun resetInputState() {

    }
}