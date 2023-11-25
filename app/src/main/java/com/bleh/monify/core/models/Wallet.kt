package com.bleh.monify.core.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
data class Wallet(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val idUser: Int,
    val name: String,
    val balance: Double,
    val icon: Int,
    val isDeleted: Boolean,
)
