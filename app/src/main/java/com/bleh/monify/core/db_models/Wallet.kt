package com.bleh.monify.core.db_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class Wallet(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val balance: Double,
    val icon: Int,
    val isDeleted: Boolean,
    )
