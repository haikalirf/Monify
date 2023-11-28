package com.bleh.monify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.bleh.monify.core.daos.BaseDao
import com.bleh.monify.core.daos.CategoryDao
import com.bleh.monify.core.daos.TransactionDao
import com.bleh.monify.core.daos.UserDao
import com.bleh.monify.core.daos.WalletDao
import com.bleh.monify.core.database.AppDatabase
import com.bleh.monify.core.helper.MockData
import com.bleh.monify.feature_analysis.analysis.AnalysisViewModel
import com.bleh.monify.feature_analysis.analysis.AnalysisScreen
import com.bleh.monify.feature_auth.AuthViewModel
import com.bleh.monify.feature_auth.GoogleAuthClient
import com.bleh.monify.feature_auth.login.LoginScreen
import com.bleh.monify.feature_auth.register.RegisterScreen
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.feature_book.book.BookScreen
import com.bleh.monify.feature_book.add.AddBookScreen
import com.bleh.monify.feature_more.budget.BudgetViewModel
import com.bleh.monify.feature_more.budget.BudgetScreen
import com.bleh.monify.feature_more.category.EditCategoryScreen
import com.bleh.monify.feature_more.category.CategoryScreen
import com.bleh.monify.feature_more.category.CategoryViewModel
import com.bleh.monify.feature_more.more.MoreViewModel
import com.bleh.monify.feature_more.more.MoreScreen
import com.bleh.monify.feature_wallet.WalletViewModel
import com.bleh.monify.feature_wallet.edit.EditWalletScreen
import com.bleh.monify.feature_wallet.wallet.WalletScreen
import com.bleh.monify.ui.theme.MonifyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authUiClient: GoogleAuthClient
    @Inject
    lateinit var appDatabase: AppDatabase
    @Inject
    lateinit var baseDao: BaseDao
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var walletDao: WalletDao
    @Inject
    lateinit var transactionDao: TransactionDao
    @Inject
    lateinit var categoryDao: CategoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            appDatabase.clearAllTables()
            baseDao.deleteAllPrimaryKeys()
            val mockData = MockData(
                userDao = userDao,
                walletDao = walletDao,
                transactionDao = transactionDao,
                categoryDao = categoryDao
            )
            mockData.insertMockData()
        }

        setContent {
            val navController = rememberNavController()
            MonifyTheme {
                NavHost(navController = navController, startDestination = "auth") {
                    navigation(startDestination = "login", route = "auth") {
                        composable(route = "login",) {
                            val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                            LoginScreen(navController = navController, viewModel = viewModel)
                        }
                        composable(route = "register") {
                            val viewModel = it.sharedViewModel<AuthViewModel>(navController)
                            RegisterScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                    navigation(startDestination = "book_main", route = "book") {
                        composable("book_main") {
                            val viewModel = it.sharedViewModel<BookViewModel>(navController)
                            BookScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("book_add") {
                            val viewModel = it.sharedViewModel<BookViewModel>(navController)
                            AddBookScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                    navigation(startDestination = "wallet_main", route = "wallet") {
                        composable("wallet_main") {
                            val viewModel = it.sharedViewModel<WalletViewModel>(navController = navController)
                            WalletScreen(navController = navController, viewModel = viewModel)
                        }
                        composable("wallet_add") {
                            val viewModel = it.sharedViewModel<WalletViewModel>(navController = navController)
                            EditWalletScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                    navigation(startDestination = "analysis_main", route = "analysis") {
                        composable("analysis_main") {
                            val viewModel = it.sharedViewModel<AnalysisViewModel>(navController = navController)
                            AnalysisScreen(navController = navController, viewModel = viewModel)
                        }
                    }
                    navigation(startDestination = "more_main", route = "more") {
                        composable("more_main") {
                            val viewModel = it.sharedViewModel<MoreViewModel>(navController = navController)
                            MoreScreen(navController = navController, viewModel = viewModel)
                        }
                        navigation(startDestination = "category_main", route = "category") {
                            composable("category_main") {
                                val viewModel = it.sharedViewModel<CategoryViewModel>(navController = navController)
                                CategoryScreen(navController = navController, viewModel = viewModel)
                            }
                            composable("category_add") {
                                val viewModel = it.sharedViewModel<CategoryViewModel>(navController = navController)
                                EditCategoryScreen(navController = navController, viewModel = viewModel)
                            }
                        }
                        navigation(startDestination = "budget_main", route = "budget") {
                            composable("budget_main") {
                                val viewModel = it.sharedViewModel<BudgetViewModel>(navController = navController)
                                BudgetScreen(navController = navController, viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel>NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}