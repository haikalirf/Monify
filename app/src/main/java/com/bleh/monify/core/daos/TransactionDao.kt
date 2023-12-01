package com.bleh.monify.core.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.bleh.monify.core.entities.TransactionEntity
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.core.pojos.CategoryWithSumAndPercentage
import com.bleh.monify.core.pojos.TransactionCategoryWallet
import com.bleh.monify.feature_book.edit.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity)

    @Query("delete from TransactionEntity where id = :id")
    suspend fun deleteTransaction(id: Int)

    @Query("SELECT * FROM TransactionEntity")
    fun getTransactions(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("SELECT * FROM TransactionEntity")
    fun getTransactionCategoryWallets(): Flow<List<TransactionCategoryWallet>>

    @Transaction
    @Query("SELECT * FROM TransactionEntity WHERE description LIKE '%' || :str || '%'")
    fun getTransactionCategoryWalletsByString(str: String): Flow<List<TransactionCategoryWallet>>

    @Query("SELECT SUM(balance) AS sum FROM TransactionEntity WHERE date BETWEEN :startDate AND :endDate AND balance > 0")
    fun sumOfPositiveInRange(startDate: LocalDate, endDate: LocalDate): Flow<Double>

    @Query("SELECT SUM(balance) AS sum FROM TransactionEntity WHERE date BETWEEN :startDate AND :endDate AND balance < 0")
    fun sumOfNegativeInRange(startDate: LocalDate, endDate: LocalDate): Flow<Double>

    @Transaction
    @Query(
        """
        SELECT Category.*, 
            SUM(TransactionEntity.balance) AS sum,
            SUM(TransactionEntity.balance) * 100.0 / total.total_sum AS percentage
        FROM TransactionEntity
        INNER JOIN Category ON TransactionEntity.CategoryId = Category.id
        CROSS JOIN (
            SELECT SUM(balance) AS total_sum
            FROM TransactionEntity
            WHERE date BETWEEN :startDate AND :endDate
                AND isTransfer = 0
                AND CategoryId IN (
                    SELECT id
                    FROM Category
                    WHERE type = :categoryType
                )
        ) AS total
        WHERE TransactionEntity.date BETWEEN :startDate AND :endDate
            AND isTransfer = 0
            AND Category.type = :categoryType
        GROUP BY Category.id
    """
    )
    fun sumOfCategoriesInRangeAndType(startDate: LocalDate, endDate: LocalDate, categoryType: CategoryType): Flow<List<CategoryWithSumAndPercentage>>
}