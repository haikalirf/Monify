package com.bleh.monify.core.db_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transfer(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
)