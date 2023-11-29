package com.bleh.monify.core.pojos

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.entities.Transaction
import com.bleh.monify.core.entities.Wallet
import java.time.LocalDate

data class TransactionCategoryWallet(
    @Embedded val transaction: Transaction,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category,
    @Relation(
        parentColumn = "walletFromId",
        entityColumn = "id"
    )
    val walletFrom: Wallet,
    @Relation(
        parentColumn = "walletToId",
        entityColumn = "id"
    )
    val walletTo: Wallet?
//    @ColumnInfo(name = "categoryName") val categoryName: String,
//    @ColumnInfo(name = "icon") val categoryIcon: Int,
//    @ColumnInfo(name = "walletName") val walletName: String,
//    @Embedded val category: Category
//    @Embedded val wallet: Wallet
)

fun List<TransactionCategoryWallet>.groupedByDay(): Map<LocalDate, List<TransactionCategoryWallet>> {
    val dataMap: MutableMap<LocalDate, MutableList<TransactionCategoryWallet>> = mutableMapOf()
    this.forEach { transaction ->
        val date = transaction.transaction.date
        if (dataMap[date] == null) {
            dataMap[date] = mutableListOf()
        }
        dataMap[date]!!.add(transaction)
    }
    dataMap.values.forEach { dayTransactions ->
        dayTransactions.sortBy { transaction -> transaction.transaction.date }
    }
    return dataMap.toSortedMap(compareByDescending { it })
}