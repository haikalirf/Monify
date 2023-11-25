package com.bleh.monify.core.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["idUser"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Wallet::class,
            parentColumns = ["id"],
            childColumns = ["idWalletFrom"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Wallet::class,
            parentColumns = ["id"],
            childColumns = ["idWalletTo"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["idCategory"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["idUser"]),
        Index(value = ["idWalletFrom"]),
        Index(value = ["idWalletTo"]),
        Index(value = ["idCategory"])
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val idUser: Int,
    val idWalletFrom: Int,
    val idWalletTo: Int,
    val idCategory: Int,
    val isTransfer: Boolean,
    val description: String,
    val balance: Double,
    val admin: Double,
    val date: LocalDate
)
