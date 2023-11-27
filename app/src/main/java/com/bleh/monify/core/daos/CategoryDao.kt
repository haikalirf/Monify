package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.enums.BudgetType
import com.bleh.monify.core.enums.CategoryType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Dao
abstract class CategoryDao: BudgetDao {
    @Upsert
    abstract suspend fun _upsertCategory(category: Category): Long

//    @Delete
//    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category")
    abstract fun getCategories(): Flow<List<Category>>

    @Transaction
    open suspend fun upsertCategoryWithBudget(category: Category) {
        val id = _upsertCategory(category)
        if(category.type == CategoryType.OUTCOME) {
            upsertBudget(
                Budget(
                    categoryId = id.toInt(),
                    weeklyAmount = null,
                    weeklyUsed = 0.0,
                    monthlyAmount = null,
                    monthlyUsed = 0.0
                )
            )
        }
    }
}