package com.bleh.monify.core.pojos

import androidx.room.Embedded
import androidx.room.Relation
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.entities.Category

data class BudgetCategory (
    @Embedded val budget: Budget,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)