package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.pojos.BudgetCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Upsert
    suspend fun upsertBudget(budget: Budget)

    @Query("SELECT * FROM budget")
    fun getBudgets(): Flow<List<Budget>>

    @Transaction
    @Query("SELECT * FROM budget")
    fun getBudgetsWithCategory(): Flow<List<BudgetCategory>>

    @Query("UPDATE budget SET weeklyAmount = :amount WHERE id = :id")
    fun updateWeeklyBudget(id: Int, amount: Double?)

    @Query("UPDATE budget SET monthlyAmount = :amount WHERE id = :id")
    fun updateMonthlyBudget(id: Int, amount: Double?)

    @Query("UPDATE budget SET weeklyUsed = weeklyUsed + :amount WHERE id = :id")
    fun addWeeklyUsed(id: Int, amount: Double?)

    @Query("UPDATE budget SET monthlyUsed = monthlyUsed + :amount WHERE id = :id")
    fun addMonthlyUsed(id: Int, amount: Double?)
}