package com.bleh.monify.core.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bleh.monify.feature_more.category.CategoryType

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["idUser"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idUser"])
    ]
)
data class Category (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val idUser: Int,
    val type: CategoryType,
    val name: String,
    val icon: Int,
    val isDeleted: Boolean,
)
