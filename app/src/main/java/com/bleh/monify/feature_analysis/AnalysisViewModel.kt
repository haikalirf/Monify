package com.bleh.monify.feature_analysis

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class AnalysisType(val value: Int, val categoryName: String, val icon: Int) {
    INCOME(0, "Pemasukan", R.drawable.ic_income),
    OUTCOME(1, "Pengeluaran", R.drawable.ic_outcome),
    BUDGET(2, "Anggaran", R.drawable.ic_budget),
    Comparison(3, "Perbandingqan", R.drawable.ic_comparison)
}

data class AnalysisState(
    val analysisType: AnalysisType = AnalysisType.OUTCOME,
)

@HiltViewModel
class AnalysisViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AnalysisState())
    val state = _state.asStateFlow()

    fun updateAnalysisType (type: AnalysisType) {
        _state.update {
            it.copy(analysisType = type)
        }
    }
}