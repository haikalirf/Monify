package com.bleh.monify.core.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bleh.monify.feature_more.budget.BudgetType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idCategory"])
    ]
)
data class Budget (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val idCategory: Int,
    val type: BudgetType,
    val balance: Double
)
