package com.bleh.monify.core.pojos

import androidx.room.Embedded
import com.bleh.monify.core.entities.Category

data class CategoryWithSumAndPercentage(
    @Embedded val category: Category,
    val sum: Double,
    val percentage: Double,
)