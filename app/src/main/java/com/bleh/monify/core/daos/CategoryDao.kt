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
    abstract suspend fun _upsertCategory(category: Category)

//    @Delete
//    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM category")
    abstract fun getCategories(): Flow<List<Category>>

    @Transaction
    open suspend fun upsertCategoryWithBudget(category: Category) {
        _upsertCategory(category)
        if(category.type == CategoryType.OUTCOME) {
            upsertBudget(
                Budget(
                    categoryId = category.id,
                    type = BudgetType.MONTHLY,
                    used = 0.0,
                )
            )
            upsertBudget(
                Budget(
                    categoryId = category.id,
                    type = BudgetType.WEEKLY,
                    used = 0.0,
                )
            )
        }
    }
}