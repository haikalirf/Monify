package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bleh.monify.core.entities.Transaction
import com.bleh.monify.core.pojos.TransactionCategoryWallet
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM 'transaction'")
    fun getTransactions(): Flow<List<Transaction>>

//    @Query(
//        "SELECT * FROM 'transaction'" +
//        "INNER JOIN (" + "" +
//            "SELECT name as categoryName, icon as iconName" +
//            "FROM category" +
//        ") ON transaction.categoryId = category.id" +
//    )
    @Query("SELECT * FROM 'transaction'")
    fun getTransactionCategoryWallets(): Flow<List<TransactionCategoryWallet>>

    @Query("SELECT * FROM 'transaction' WHERE description LIKE '%' || :str || '%'")
    fun getTransactionCategoryWalletsByString(str: String): Flow<List<TransactionCategoryWallet>>
}