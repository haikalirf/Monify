package com.bleh.monify.core.db_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bleh.monify.feature_more.category.CategoryType

@Entity
data class Category (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val type: CategoryType,
    val name: String,
    val icon: Int,
    val isDeleted: Boolean,
)
