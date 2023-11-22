package com.bleh.monify.core.db_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bleh.monify.feature_more.budget.BudgetType
import java.math.BigDecimal

@Entity
data class Budget (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val type: BudgetType,
    val balance: Double
)
