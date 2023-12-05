package com.bleh.monify.feature_wallet.edit

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.ButtonCombinations
import com.bleh.monify.feature_wallet.WalletViewModel
import com.bleh.monify.feature_wallet.helper.walletIconList
import com.bleh.monify.ui.theme.Grey

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditWalletScreen(
    navController: NavController,
    viewModel: WalletViewModel,
) {
    BackHandler {
        viewModel.resetState()
        navController.popBackStack()
    }
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
                viewModel = viewModel,
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
    viewModel: WalletViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
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
                text = if(state.isEdit) "Ubah / Hapus Dompet" else "Tambah Dompet",
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
    val context = LocalContext.current
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
            singleLine = true,
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
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
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
        ButtonCombinations(
            backButton = {
                viewModel.resetState()
                navController.popBackStack()
            },
            addButton = {
                if(viewModel.upsertWallet(context) == null) {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            },
            saveButton = {
                if(viewModel.upsertWallet(context) == null) {
                    viewModel.resetState()
                    navController.popBackStack()
                }
            },
            deleteButton = {
                viewModel.setDeletedTrue(state.currentEditId)
                viewModel.resetState()
                navController.popBackStack()
            },
            isEdit = state.isEdit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
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
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        walletIconList().forEachIndexed { index, it ->
            item {
                WalletItem(
                    icon = it,
                    isSelected = index == state.selectedWallet
                ) {
                    viewModel.updateSelectedWalletState(index)
                    Log.d("WalletIcon", "WalletIcon: $index")
                }
            }
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