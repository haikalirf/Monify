package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.bleh.monify.core.entities.Transaction
import com.bleh.monify.core.pojos.TransactionCategoryWallet
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)

    @Query("delete from 'transaction' where id = :id")
    suspend fun deleteTransaction(id: Int)

    @Query("SELECT * FROM 'transaction'")
    fun getTransactions(): Flow<List<Transaction>>

//    @Query(
//        "SELECT * FROM 'transaction'" +
//        "INNER JOIN (" + "" +
//            "SELECT name as categoryName, icon as iconName" +
//            "FROM category" +
//        ") ON transaction.categoryId = category.id" +
//    )
    @androidx.room.Transaction
    @Query("SELECT * FROM 'transaction'")
    fun getTransactionCategoryWallets(): Flow<List<TransactionCategoryWallet>>

    @androidx.room.Transaction
    @Query("SELECT * FROM 'transaction' WHERE description LIKE '%' || :str || '%'")
    fun getTransactionCategoryWalletsByString(str: String): Flow<List<TransactionCategoryWallet>>

    @Query("SELECT SUM(balance) AS sum FROM 'transaction' WHERE date BETWEEN :startDate AND :endDate AND balance > 0")
    fun sumOfPositiveInRange(startDate: LocalDate, endDate: LocalDate): Flow<Double>

    @Query("SELECT SUM(balance) AS sum FROM 'transaction' WHERE date BETWEEN :startDate AND :endDate AND balance < 0")
    fun sumOfNegativeInRange(startDate: LocalDate, endDate: LocalDate): Flow<Double>
}