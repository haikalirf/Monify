package com.bleh.monify.core.helper

import com.bleh.monify.R
import com.bleh.monify.core.daos.BudgetDao
import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.entities.Category
import com.bleh.monify.core.entities.Transaction
import com.bleh.monify.core.entities.User
import com.bleh.monify.core.entities.Wallet
import com.bleh.monify.core.enums.CategoryType
import javax.inject.Inject

class MockData @Inject constructor(
    private val userDao: UserDao,
    private val walletDao: WalletDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) {
    val walletList = listOf<Wallet>(
        Wallet(
            userId = 1,
            name = "BCA",
            balance = 5000000.0,
            icon = R.drawable.bca,
            isDeleted = false
        ),
        Wallet(
            userId = 1,
            name = "Dana",
            balance = 1000000.0,
            icon = R.drawable.dana,
            isDeleted = false
        ),
        Wallet(
            userId = 1,
            name = "Gopay",
            balance = 2000000.0,
            icon = R.drawable.gopay,
            isDeleted = false
        ),
    )
    val categoryList = listOf<Category>(
        Category(
            userId = 1,
            name = "Belanja",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_shopping_cart,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Kesehatan",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_health,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Laundry",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_shirt,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Listrik",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_electricity,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Makanan & Minuman",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_food,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Olahraga",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_gym,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Paket Data",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_mobile_data,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Pendidikan",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_education,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Transportasi",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_transportation,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "WiFi",
            type = CategoryType.OUTCOME,
            icon = R.drawable.ic_wifi,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Gaji",
            type = CategoryType.INCOME,
            icon = R.drawable.ic_person_blackboard,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Uang Bulanan",
            type = CategoryType.INCOME,
            icon = R.drawable.ic_money,
            isDeleted = false
        ),
        Category(
            userId = 1,
            name = "Beasiswa",
            type = CategoryType.INCOME,
            icon = R.drawable.ic_paper_checkmark,
            isDeleted = false
        ),
    )
//    val transactionList = listOf<Transaction>(
//        Transaction(
//            userId = 1,
//            walletId = 1,
//            categoryId = 1,
//            amount = 100000.0,
//            description = "Makanan",
//            isDeleted = false
//        ),
//    )
    suspend fun insertMockData() {
        userDao.upsertUser(
            User(
                id = 1,
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
    }
}