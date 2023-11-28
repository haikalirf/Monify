package com.bleh.monify.core.helper

import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.entities.Transaction
import com.bleh.monify.core.entities.User
import com.bleh.monify.core.entities.Wallet
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.feature_more.category.helper.categoryIconList
import com.bleh.monify.feature_wallet.helper.walletIconList
import io.github.serpro69.kfaker.Faker
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class MockData @Inject constructor(
    private val userDao: UserDao,
    private val walletDao: WalletDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) {
    private val faker = Faker()
    private val walletIconList = walletIconList()
    private val categoryIconList = categoryIconList()
    private val walletList = walletIconList.map {
        Wallet(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            name = faker.random.randomString(min = 5, max = 20),
            balance = faker.random.nextInt(min = 10000, max = 100000000) + faker.random.nextDouble(),
            icon = it,
            isDeleted = false
        )
    }
    private val categoryList = categoryIconList.map {
        Category(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            name = faker.random.randomString(min = 5, max = 20),
            type = faker.random.nextEnum<CategoryType>(),
            icon = it,
            isDeleted = false
        )
    }
    private val transactionList = List(30) {
        Transaction(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            walletFromId = faker.random.nextInt(min = 1, max = 3),
            walletToId = null,
            categoryId = faker.random.nextInt(min = 1, max = 13),
            isTransfer = false,
            description = faker.random.randomString(min = 5, max = 20),
            balance = faker.random.nextInt(min = 10000, max = 10000000) + faker.random.nextDouble(),
            admin = null,
            date = LocalDate.now().minus(
                faker.random.nextInt(min = 0, max = 5).toLong(),
                ChronoUnit.DAYS
            )
        )
    }
    private val transferList = List(10) {
        Transaction(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            walletFromId = faker.random.nextInt(min = 1, max = 3),
            walletToId = faker.random.nextInt(min = 1, max = 3),
            categoryId = faker.random.nextInt(min = 1, max = 13),
            isTransfer = true,
            description = faker.random.randomString(min = 5, max = 20),
            balance = faker.random.nextInt(min = 10000, max = 10000000) + faker.random.nextDouble(),
            admin = faker.random.nextInt(min = 1000, max = 10000) + faker.random.nextDouble(),
            date = LocalDate.now().minus(
                faker.random.nextInt(min = 0, max = 5).toLong(),
                ChronoUnit.DAYS
            )
        )
    }
    suspend fun insertMockData() {
        userDao.upsertUser(
            User(
                id = "pijBlqHvlmWJkUO8LWckenhzob02",
                email = "fakeEmail"
            )
        )
        walletList.forEach {
            walletDao.upsertWallet(
                it
            )
        }
        categoryList.forEach {
            categoryDao.upsertCategoryWithBudget(
                it
            )
        }
        transactionList.forEach {
            transactionDao.upsertTransaction(
                it
            )
        }
        transferList.forEach {
            transactionDao.upsertTransaction(
                it
            )
        }
    }
}