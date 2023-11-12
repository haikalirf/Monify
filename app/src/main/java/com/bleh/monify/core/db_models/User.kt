package com.bleh.monify.core.db_models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val email: String,
)
