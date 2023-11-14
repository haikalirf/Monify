package com.bleh.monify.feature_analysis.transaction

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import com.bleh.monify.feature_analysis.analysis.AnalysisType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AnalysisTransaction(
    val icon: Int,
    val name: String,
    val amount: String,
    val date: String,
    val type: AnalysisType,
)

data class AnalysisTransactionState(
    val analysisType: AnalysisType = AnalysisType.OUTCOME,
    val analysisTransactionList: List<AnalysisTransaction> = listOf(
        AnalysisTransaction(
            icon = R.drawable.ic_person_blackboard,
            name = "Uang Bulanan",
            amount = "Rp 800,000.00",
            date = "2023-11",
            AnalysisType.INCOME
        ),
        AnalysisTransaction(
            icon = R.drawable.ic_money,
            name = "Gaji",
            amount = "Rp 200,000.00",
            date = "2023-11",
            AnalysisType.INCOME
        )
    ),
)

@HiltViewModel
class AnalysisTransactionViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AnalysisTransactionState())
    val state = _state.asStateFlow()

    fun updateAnalysisType (type: AnalysisType) {
        _state.update {
            it.copy(analysisType = type)
        }
    }
}