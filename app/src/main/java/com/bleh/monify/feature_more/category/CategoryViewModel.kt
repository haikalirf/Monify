package com.bleh.monify.feature_more.category

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bleh.monify.R
import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.feature_auth.GoogleAuthClient
import com.bleh.monify.feature_more.category.helper.categoryIconList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryState(
    val categoryType: CategoryType = CategoryType.OUTCOME,
    val categoryName: String = "testemail@gmail.com",
    val selectedCategory: Int = 0,
    val categories: List<Category> = listOf(),
    val isEdit: Boolean = false,
    val currentEditId: Int = 0,
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val googleAuthClient: GoogleAuthClient,
    private val categoryDao: CategoryDao
): ViewModel() {
    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()
    private val currentUser = googleAuthClient.getLoggedInUser()
    private val iconList = categoryIconList()

    init {
        getCategory()
    }

    private fun getCategory () {
        viewModelScope.launch {
            categoryDao.getCategories(currentUser!!.userId).flowOn(Dispatchers.IO).collect { categories ->
                _state.update {
                    it.copy(categories = categories)
                }
            }
        }
    }

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

    fun updateIsEditState (isEdit: Boolean) {
        _state.update {
            it.copy(isEdit = isEdit)
        }
    }

    fun updateCurrentEditIdState (currentEditId: Int) {
        _state.update {
            it.copy(currentEditId = currentEditId)
        }
    }

    fun upsertCategory (context: Context): Exception? {
        if (_state.value.categoryName.isEmpty()) {
            Toast.makeText(context, "Nama Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return Exception("Nama Kategori tidak boleh kosong")
        }
        var err: Exception? = null
        val state = _state.value
        val category = Category(
            id = if(state.isEdit) state.currentEditId else 0,
            userId = currentUser!!.userId,
            name = state.categoryName,
            type = state.categoryType,
            icon = iconList[state.selectedCategory],
            isDeleted = false
        )
        viewModelScope.launch {
            try {
                if (state.isEdit) {
                    categoryDao.upsertCategory(category)
                } else {
                    categoryDao.upsertCategoryWithBudget(category)
                }
            } catch (e: Exception) {
                Log.d("CategoryViewModel", "upsertCategory: $e")
                Toast.makeText(context, "Unexpected Error", Toast.LENGTH_SHORT).show()
                err = e
            }
        }
        return err
    }

    fun setDeletedTrue (id: Int) {
        viewModelScope.launch {
            categoryDao.setDeletedTrue(id)
        }
    }

    fun resetInputState() {
        _state.update {
            it.copy(
                categoryName = "",
                selectedCategory = 0,
                isEdit = false
            )
        }
    }
}