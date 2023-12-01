package com.bleh.monify.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bleh.monify.core.daos.BaseDao
import com.bleh.monify.core.daos.BudgetDao
import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.entities.TransactionEntity
import com.bleh.monify.core.entities.User
import com.bleh.monify.core.entities.Wallet

@Database(
    entities = [
        User::class,
        Category::class,
        Wallet::class,
        TransactionEntity::class,
        Budget::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun baseDao(): BaseDao
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun walletDao(): WalletDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
}