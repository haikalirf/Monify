package com.bleh.monify.feature_more.category

import androidx.lifecycle.ViewModel
import com.bleh.monify.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

enum class CategoryType(val value: Int, val categoryName: String, val icon: Int) {
    INCOME(0, "Pemasukan", R.drawable.ic_income),
    OUTCOME(1, "Pengeluaran", R.drawable.ic_outcome)
}

data class CategoryState(
    val categoryType: CategoryType = CategoryType.OUTCOME,
    val categoryName: String = "testemail@gmail.com",
    val selectedCategory: Int = 0,
    val addCategoryType: CategoryType = CategoryType.OUTCOME,
)

@HiltViewModel
class CategoryViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    fun updateCategoryType (type: CategoryType) {
        _state.update {
            it.copy(categoryType = type)
        }
    }

    fun updateCategoryName (name: String) {
        _state.update {
            it.copy(categoryName = name)
        }
    }

    fun updateSelectedCategory (category: Int) {
        _state.update {
            it.copy(selectedCategory = category)
        }
    }

    fun updateAddCategoryType (type: CategoryType) {
        _state.update {
            it.copy(addCategoryType = type)
        }
    }

    fun resetInputState() {
        _state.update {
            it.copy(
                categoryName = "",
                selectedCategory = 0,
            )
        }
    }
}