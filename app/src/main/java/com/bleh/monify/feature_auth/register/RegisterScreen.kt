package com.bleh.monify.feature_auth.register

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.feature_auth.AuthViewModel
import com.bleh.monify.feature_auth.RegisterResult
import com.bleh.monify.ui.theme.accent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val email = state.emailState
    val password = state.passwordState
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.register_already_have_account))
        pushStringAnnotation("login", "login")
        pushStyle(SpanStyle(
            fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
            fontWeight = FontWeight(600),
        ))
        append(stringResource(R.string.register_login))
        pop()
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
            RegisterCard(
                annotatedString = annotatedString,
                emailState = email,
                onEmailChange = {newEmail ->
                    viewModel.updateEmailState(newEmail)
                },
                passwordState = password,
                onPasswordChange = {newPassword ->
                    viewModel.updatePasswordState(newPassword)
                },
                signUpButtonClick = {
                    focusManager.clearFocus()
                    coroutineScope.launch {
                        val signUpResult = viewModel.onSignUpClick(context = navController.context)
                        val error = signUpResult.errorMessage
                        Toast.makeText(
                            navController.context,
                            signUpResult.errorMessage ?: "Sign up successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (error == null) {
                            viewModel.resetState()
                            navController.navigate("login")
                        }
                    }
                },
                loginButtonClick = { offset ->
                    annotatedString.getStringAnnotations(
                        tag = "login",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCard(
    modifier: Modifier = Modifier,
    emailState: String,
    onEmailChange: (String) -> Unit,
    passwordState: String,
    onPasswordChange: (String) -> Unit,
    annotatedString: AnnotatedString,
    signUpButtonClick: () -> Unit,
    loginButtonClick: (Int) -> Unit
) {
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
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                value = emailState,
                onValueChange = onEmailChange,
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 40.dp)
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(top = 24.dp)
            )
            AccentedButton(
                onClick = signUpButtonClick,
                text = stringResource(R.string.register_button),
                modifier = Modifier
                    .padding(top = 40.dp)
            )
            ClickableText(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                onClick = loginButtonClick,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .width(240.dp)
            )
        }
    }
}