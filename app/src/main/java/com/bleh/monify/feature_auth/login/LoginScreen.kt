package com.bleh.monify.feature_auth.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.feature_auth.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val email = state.emailState
    val password = state.passwordState
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.login_dont_have_account))
        pushStringAnnotation("signup", "signup")
        pushStyle(SpanStyle(
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = FontWeight(600),
        ))
        append(stringResource(R.string.login_sign_up))
        pop()
    }
    LaunchedEffect(Unit) {
        if (state.isUserLoggedIn) {
            navController.navigate("book") {
                popUpTo("auth") {
                    inclusive = true
                }
            }
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.main_background),
                contentScale = ContentScale.Crop
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            LoginCard(
                annotatedString = annotatedString,
                emailState = email,
                onEmailChange = {newEmail ->
                    viewModel.updateEmailState(newEmail)
                },
                passwordState = password,
                onPasswordChange = {newPassword ->
                    viewModel.updatePasswordState(newPassword)
                },
                loginButtonClick = {
                    focusManager.clearFocus()
                    coroutineScope.launch {
                        val loginResult = viewModel.onLoginClick(navController.context)
                        val error = loginResult.errorMessage
                        Toast.makeText(
                            navController.context,
                            error ?: "Login successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (loginResult.data != null) {
                            navController.navigate("book_main") {
                                popUpTo("auth") {
                                    inclusive = true
                                }
                            }
                        }
                    }
                },
                signUpButtonClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "signup",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        navController.navigate("register")
                        viewModel.resetState()
                    }
                }
            )
        }
    }
}

@Composable
fun LoginCard(
    modifier: Modifier = Modifier,
    emailState: String,
    onEmailChange: (String) -> Unit,
    passwordState: String,
    onPasswordChange: (String) -> Unit,
    annotatedString: AnnotatedString,
    loginButtonClick: () -> Unit,
    signUpButtonClick: (Int) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(1.dp, Color.Black),
        modifier = modifier
            .defaultMinSize(minHeight = 400.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .padding(horizontal = 12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                text = stringResource(R.string.login_card_title),
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                value = emailState,
                onValueChange = onEmailChange,
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 40.dp)
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = onPasswordChange,
                maxLines = 1,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible) R.drawable.ic_eye else R.drawable.ic_eye_crossed
                            ),
                            contentDescription = "Password Visibility Toggle"
                        )
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 24.dp)
            )
            AccentedButton(
                onClick = loginButtonClick,
                text = stringResource(R.string.login_button),
                modifier = Modifier
                    .padding(top = 40.dp)
            )
            ClickableText(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                onClick = signUpButtonClick,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .width(200.dp)
            )
        }
    }
}