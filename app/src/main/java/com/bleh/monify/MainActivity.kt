package com.bleh.monify

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bleh.monify.feature_auth.AuthViewModel
import com.bleh.monify.feature_auth.GoogleAuthUiClient
import com.bleh.monify.feature_auth.login.LoginScreen
import com.bleh.monify.feature_auth.register.RegisterScreen
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.feature_book.book.BookScreen
import com.bleh.monify.feature_book.editor.AddBookScreen
import com.bleh.monify.ui.theme.MonifyTheme
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonifyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "book") {
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
                    navigation(startDestination = "book_add", route = "book") {
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

                        }
                    }
                    navigation(startDestination = "analysis_main", route = "analysis") {
                        composable("analysis_main") {

                        }
                    }
                    navigation(startDestination = "settings_main", route = "settings") {
                        composable("settings_main") {

                        }
                    }
                }
            }
        }
//        window.fitSystemWindowsWithAdjustResize()
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