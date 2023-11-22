package com.bleh.monify.feature_wallet.add

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.feature_wallet.WalletViewModel
import com.bleh.monify.ui.theme.Grey

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddWalletScreen(
    navController: NavController,
    viewModel: WalletViewModel,
) {
    Scaffold(
        containerColor = Color.Transparent,
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.main_background),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
        ) {
            AddWalletComposable(
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            AddWalletCard(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier
                    .padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun AddWalletComposable(
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(55.dp)
            .clip(shape = RoundedCornerShape(30.dp))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Tambah Dompet",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun AddWalletCard(
    viewModel: WalletViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Nama Dompet",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = state.walletName,
            onValueChange = {
                viewModel.updateWalletNameState(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Text(
            text = "Nominal",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = state.walletNominal,
            onValueChange = {
                viewModel.updateWalletNominalState(it)
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Text(
            text = "Ikon",
            style = MaterialTheme.typography.bodyLarge,
        )
        WalletGrid(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            AccentedButton(
                onClick = {
                    navController.navigate("wallet_main") {
                        popUpTo("wallet_main") {
                            inclusive = true
                        }
                    }
                },
                text = "Kembali",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
            AccentedButton(
                onClick = { /*TODO*/ },
                text = "Tambah",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
        }
    }
}

@Composable
fun WalletGrid(
    viewModel: WalletViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        items(4) {
            Spacer(modifier = Modifier.size(20.dp))
        }
        items(20) {
            WalletItem(
                icon = R.drawable.bca,
                isSelected = it == state.selectedWallet
            ) {
                viewModel.updateSelectedWalletState(it)
            }
        }
        items(4) {
            Spacer(modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun WalletItem(
    icon: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Grey else Color.Transparent
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                onClick()
            }
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .padding(10.dp)
            .size(70.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Wallet Icon",
            modifier = Modifier
                .size(36.dp)
        )
    }
}

//@Preview
@Composable
fun AddWalletScreenPreview() {
    AddWalletScreen(
        navController = NavController(LocalContext.current),
        viewModel = WalletViewModel()
    )
}