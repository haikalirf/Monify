package com.bleh.monify.core.helper

import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.entities.TransactionEntity
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
            name = faker.random.randomString(min = 3, max = 10),
            balance = faker.random.nextInt(min = 10000, max = 100000000) + faker.random.nextDouble(),
            icon = it,
            isDeleted = false
        )
    }
    private val categoryList = categoryIconList.mapIndexed { index, icon ->
        Category(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            name = faker.random.randomString(min = 5, max = 15),
            type = if (index < 6) CategoryType.OUTCOME else CategoryType.INCOME,
            icon = icon,
            isDeleted = false
        )
    }
    private val transactionList1 = List(15) {
        TransactionEntity(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            walletFromId = faker.random.nextInt(min = 1, max = 3),
            walletToId = null,
            categoryId = faker.random.nextInt(min = 7, max = 13),
            isTransfer = false,
            description = faker.random.randomString(min = 5, max = 15),
            balance = faker.random.nextInt(min = 1000, max = 10000000) + faker.random.nextDouble(),
            admin = null,
            date = LocalDate.now().minus(
                faker.random.nextInt(min = 0, max = 5).toLong(),
                ChronoUnit.DAYS
            )
        )
    }
    private val transferList = List(10) {
        TransactionEntity(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            walletFromId = faker.random.nextInt(min = 1, max = 3),
            walletToId = faker.random.nextInt(min = 1, max = 3),
            categoryId = null,
            isTransfer = true,
            description = faker.random.randomString(min = 5, max = 15),
            balance = faker.random.nextInt(min = 1000, max = 10000000) + faker.random.nextDouble(),
            admin = faker.random.nextInt(min = 1000, max = 10000) + faker.random.nextDouble(),
            date = LocalDate.now().minus(
                faker.random.nextInt(min = 0, max = 5).toLong(),
                ChronoUnit.DAYS
            )
        )
    }
    private val transactionList2 = List(15) {
        TransactionEntity(
            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
            walletFromId = faker.random.nextInt(min = 1, max = 3),
            walletToId = null,
            categoryId = faker.random.nextInt(min = 1, max = 6),
            isTransfer = false,
            description = faker.random.randomString(min = 5, max = 15),
            balance = faker.random.nextInt(min = -10000000, max = -1000) + faker.random.nextDouble(),
            admin = null,
            date = LocalDate.now().minus(
                faker.random.nextInt(min = 0, max = 5).toLong(),
                ChronoUnit.DAYS
            )
        )
    }
    private val transactionList = transactionList1 + transferList + transactionList2

//    private val walletList = listOf<Wallet>(
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "BCA",
//            balance = 1000000.0,
//            icon = walletIconList[0],
//            isDeleted = false
//        ),
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "BRI",
//            balance = 2000000.0,
//            icon = walletIconList[1],
//            isDeleted = false
//        ),
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "BSI",
//            balance = 500000.0,
//            icon = walletIconList[2],
//            isDeleted = false
//        ),
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "DANA",
//            balance = 1000000.0,
//            icon = walletIconList[3],
//            isDeleted = false
//        ),
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "GOPAY",
//            balance = 2000000.0,
//            icon = walletIconList[4],
//            isDeleted = false
//        ),
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "OVO",
//            balance = 500000.0,
//            icon = walletIconList[5],
//            isDeleted = false
//        ),
//        Wallet(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "PANIN",
//            balance = 1000000.0,
//            icon = walletIconList[6],
//            isDeleted = false
//        ),
//    )
//    private val categoryList = listOf<Category>(
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Makanan",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[0],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Minuman",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[1],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Transportasi",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[2],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Belanja",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[3],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Hiburan",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[4],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Pendidikan",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[5],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Kesehatan",
//            type = CategoryType.OUTCOME,
//            icon = categoryIconList[6],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Gaji",
//            type = CategoryType.INCOME,
//            icon = categoryIconList[7],
//            isDeleted = false
//        ),
//        Category(
//            userId = "pijBlqHvlmWJkUO8LWckenhzob02",
//            name = "Hadiah",
//            type = CategoryType.INCOME,
//            icon = categoryIconList[8],
//            isDeleted = false
//        ),
//    )
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
    }
}