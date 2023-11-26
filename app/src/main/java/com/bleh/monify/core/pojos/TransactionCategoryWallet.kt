package com.bleh.monify.core.pojos

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.bleh.monify.core.entities.Transaction

data class TransactionCategoryWallet (
    @Embedded val transaction: Transaction,
    @ColumnInfo(name = "categoryName") val categoryName: String,
    @ColumnInfo(name = "icon") val categoryIcon: Int,
    @ColumnInfo(name = "walletName") val walletName: String,
//    @Embedded val category: Category,
//    @Embedded val wallet: Wallet
)