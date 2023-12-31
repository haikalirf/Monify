package com.bleh.monify.feature_book.edit

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.entities.Wallet
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.ui_components.ButtonCombinations
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.ui.theme.Accent
import com.bleh.monify.ui.theme.Grey
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId

enum class TransactionType(val value: Int, val transactionName: String, val icon: Int) {
    INCOME(0, "Pemasukan", R.drawable.ic_income),
    OUTCOME(1, "Pengeluaran", R.drawable.ic_outcome),
    TRANSFER(2, "Transfer", R.drawable.ic_transfer)
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditBookScreen(
    navController: NavController,
    viewModel: BookViewModel
) {
    val state by viewModel.state.collectAsState()
//    viewModel.logAllStates()
    BackHandler {
        viewModel.resetInputState()
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
        val pagerState = rememberPagerState(
            initialPage = 1,
        ) { 3 }
        LaunchedEffect(pagerState.currentPage) {
            if (!state.isEdit) {
                delay(200)
                viewModel.resetInputState()
                viewModel.updateTransactionType(pagerState.currentPage)
            }
        }
        val animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        LaunchedEffect(state.transactionType) {
            if (!state.isEdit) {
                pagerState.animateScrollToPage(
                    page = state.transactionType,
                    animationSpec = animationSpec
                )
            }
        }
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth()
            ) {
                if (state.isEdit) {
                    EditTransactionComposable(
                        viewModel = viewModel,
                        modifier = Modifier
                    )
                } else {
                    SelectionBar(
                        selectedTabPosition = state.transactionType,
                        indicatorColor = Accent,
                        fixedSize = true,
                        modifier = Modifier
                            .scale(0.95f)
                    ) {
                        for (type in enumValues<TransactionType>()) {
                            TabTitle(
                                title = type.transactionName,
                                icon = type.icon,
                                position = type.value,
                                textColor = Color.Black,
                                modifier = Modifier
                                    .height(50.dp)
                                    .scale(0.95f)
                            ) {
                                viewModel.updateTransactionType(type.value)
                            }
                        }
                    }
                }
            }
            if (state.isEdit) {
                when (state.transactionType) {
                    0 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.INCOME, navController = navController)
                    1 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.OUTCOME, navController = navController)
                    2 -> TransferCard(viewModel = viewModel, transactionType = TransactionType.TRANSFER, navController = navController)
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    beyondBoundsPageCount = 2
                ) {
                    when (it) {
                        0 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.INCOME, navController = navController)
                        1 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.OUTCOME, navController = navController)
                        2 -> TransferCard(viewModel = viewModel, transactionType = TransactionType.TRANSFER, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun EditTransactionComposable(
    viewModel: BookViewModel,
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
                text = if(state.isEdit) "Ubah / Hapus Transaksi" else "Tambah Dompet",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun TransactionCard(
    viewModel: BookViewModel,
    transactionType: TransactionType,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val note = state.note
    val nominal = state.nominal
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Catatan " + if (transactionType == TransactionType.INCOME) "Pemasukan" else "Pengeluaran",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                viewModel.updateNoteState(it)
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
            value = nominal,
            onValueChange = {
                viewModel.updateNominalState(it)
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Text(
            text = "Tanggal",
            style = MaterialTheme.typography.bodyLarge,
        )
        DatePickerComposable(
            viewModel = viewModel,
            transactionType = transactionType
        )
        Text(
            text = "Dompet",
            style = MaterialTheme.typography.bodyLarge,
        )
        WalletDropDown(
            viewModel = viewModel,
            transactionType = transactionType
        )
        Text(
            text = "Kategori",
            style = MaterialTheme.typography.bodyLarge,
        )
        CategoryGrid(
            viewModel = viewModel,
            modifier = Modifier
                .weight(1f)
        )
        ButtonCombinations(
            backButton = {
                viewModel.resetInputState()
                navController.popBackStack()
            },
            addButton = {
                val err = viewModel.upsertTransaction(context)
                if(err == null) {
//                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            saveButton = {
                val err = viewModel.upsertTransaction(context)
                if(err == null) {
//                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            deleteButton = {
                val err = viewModel.deleteTransaction(context, state.currentTransactionId)
                if(err == null) {
//                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            isEdit = state.isEdit,
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun TransferCard(
    viewModel: BookViewModel,
    transactionType: TransactionType,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val note = state.note
    val nominal = state.nominal
    val admin = state.admin
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Catatan Transfer",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = note,
            onValueChange = {
                viewModel.updateNoteState(it)
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
            value = nominal,
            onValueChange = {
                viewModel.updateNominalState(it)
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Text(
            text = "Tanggal",
            style = MaterialTheme.typography.bodyLarge,
        )
        DatePickerComposable(
            viewModel = viewModel,
            transactionType = transactionType
        )
        TransferWalletComposable(viewModel = viewModel)
        Text(
            text = "Biaya Admin",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = admin,
            onValueChange = {
                viewModel.updateAdminState(it)
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonCombinations(
            backButton = {
                viewModel.resetInputState()
                navController.popBackStack()
            },
            addButton = {
                val err = viewModel.upsertTransaction(context)
                if(err == null) {
//                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            saveButton = {
                val err = viewModel.upsertTransaction(context)
                if(err == null) {
//                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            deleteButton = {
                val err = viewModel.deleteTransaction(context, state.currentTransactionId)
                if(err == null) {
//                    viewModel.resetInputState()
                    navController.popBackStack()
                }
            },
            isEdit = state.isEdit,
            modifier = Modifier
                .padding(top = 10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComposable(
    viewModel: BookViewModel,
    transactionType: TransactionType
) {
    val state by viewModel.state.collectAsState()
    val datePickerState = rememberDatePickerState()
    OutlinedTextField(
        value = state.formattedDate,
        onValueChange = {},
        shape = RoundedCornerShape(30.dp),
        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.Black,
        ),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            platformStyle = PlatformTextStyle(
                includeFontPadding = false
            )
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                contentDescription = "Date Picker",
                modifier = Modifier
                    .size(24.dp)
            )
        },
        readOnly = true,
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            viewModel.updateShowDatePicker(true)
                        }
                    }
                }
            },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(bottom = 5.dp)
    )
    val onDismiss = {
        viewModel.updateShowDatePicker(false)
    }
    if (state.showDatePicker && state.transactionType == transactionType.value) {
        DatePickerDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                        val selectedDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val selectedDateInLocalDate = Instant.ofEpochMilli(selectedDate).atZone(ZoneId.systemDefault()).toLocalDate()
                        viewModel.updatePickedDateState(selectedDateInLocalDate)
                        viewModel.updateFormattedDate(selectedDateInLocalDate)
                        onDismiss()
                    }
                ) {
                    Text(text = "OK", color = Accent)
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                    onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(text = "Cancel", color = Accent)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedYearContainerColor = Accent,
                    selectedDayContainerColor = Accent,
                    todayDateBorderColor = Accent
                )
            )
        }
    }
}

@Composable
fun TransferWalletComposable(
    viewModel: BookViewModel,
) {
    val state by viewModel.state.collectAsState()
    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .height(70.dp)
                .width(160.dp)
        ) {
            Text(
                text = "Dompet Asal",
                style = MaterialTheme.typography.bodyLarge,
            )
            TransferWalletDropDown(
                wallet = state.walletSource,
                isExpanded = state.isWalletSourceExpanded,
                walletList = state.walletList,
                onExpandedChange = {
                    viewModel.updateWalletSourceExpanded(it)
                },
                onWalletSelected = {
                    viewModel.updateWalletSource(it)
                    viewModel.updateWalletSourceId(it.id)
                },
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "Right Arrow",
            modifier = Modifier
                .size(24.dp)
                .offset(y = (-12).dp)
        )
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .height(70.dp)
                .width(160.dp)
        ) {
            Text(
                text = "Dompet Tujuan",
                style = MaterialTheme.typography.bodyLarge,
            )
            TransferWalletDropDown(
                wallet = state.walletDestination,
                isExpanded = state.isWalletDestinationExpanded,
                walletList = state.walletList,
                onExpandedChange = {
                    viewModel.updateWalletDestinationExpanded(it)
                },
                onWalletSelected = {
                    viewModel.updateWalletDestination(it)
                    viewModel.updateWalletDestinationId(it.id)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferWalletDropDown(
    wallet: Wallet?,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    walletList: List<Wallet> = listOf(),
    onExpandedChange: (Boolean) -> Unit,
    onWalletSelected: (Wallet) -> Unit
) {
    val formatter = indonesianFormatter()
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            onExpandedChange(it)
        },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(horizontal = 5.dp)
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(30.dp)
                )
                .menuAnchor()
        ) {
            if (wallet == null) {
                Text(
                    text = "Pilih Dompet",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                ) {
                    Image(
                        painter = painterResource(id = wallet.icon),
                        contentDescription = "wallet",
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = wallet.name,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        Text(
                            text = formatter.format(wallet.balance),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
            Icon(
                painter = painterResource(id = if (isExpanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                contentDescription = "Dropdown",
                modifier = Modifier
                    .size(24.dp)
            )
        }
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(24.dp)
            )
        ) {
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                ),
                offset = DpOffset(x = 5.dp, y = 0.dp),
                modifier = Modifier
                    .exposedDropdownSize()
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .height(180.dp)
            ) {
                walletList.forEach { walletIt ->
                    TransferWalletDropDownItem(wallet = walletIt) {
                        onWalletSelected(walletIt)
                        onExpandedChange(false)
                    }
                }
            }
        }
    }
}

@Composable
fun TransferWalletDropDownItem(
    wallet: Wallet,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val formatter = indonesianFormatter()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(id = wallet.icon),
            contentDescription = "wallet",
            modifier = Modifier
                .size(24.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 10.dp)
        ) {
            Text(
                text = wallet.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = formatter.format(wallet.balance),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDropDown(
    viewModel: BookViewModel,
    transactionType: TransactionType
) {
    val state by viewModel.state.collectAsState()
    val formatter = indonesianFormatter()
    ExposedDropdownMenuBox(
        expanded = state.isWalletExpanded && state.transactionType == transactionType.value,
        onExpandedChange = {
            viewModel.updateWalletExpanded(!state.isWalletExpanded)
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.walletSource?.name?: "",
            onValueChange = {},
            shape = RoundedCornerShape(30.dp),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            leadingIcon = {
                if (state.walletSource != null) {
                    Image(
                        painter = painterResource(id = state.walletSource?.icon!!),
                        contentDescription = "wallet",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (state.isWalletExpanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .size(24.dp)
                )
            },
            readOnly = true,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
                .menuAnchor()
        )
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(24.dp)
            )
        ) {
            DropdownMenu(
                expanded = state.isWalletExpanded && state.transactionType == transactionType.value,
                onDismissRequest = {
                    viewModel.updateWalletExpanded(false)
                },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                ),
                offset = DpOffset(x = 0.dp, y = (-5).dp),
                modifier = Modifier
                    .exposedDropdownSize()
                    .background(Color.White)
                    .height(280.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                state.walletList.forEach { wallet ->
                    WalletDropDownItem(
                        icon = wallet.icon,
                        walletName = wallet.name,
                        money = formatter.format(wallet.balance)
                    ) {
                        viewModel.updateWalletSource(wallet)
                        viewModel.updateWalletSourceId(wallet.id)
                        viewModel.updateWalletExpanded(false)
                    }
                }
            }
        }
    }
}

@Composable
fun WalletDropDownItem(
    icon: Int,
    walletName: String,
    money: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        leadingIcon = {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "wallet",
                modifier = Modifier
                    .size(24.dp)
            )
        },
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = walletName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = money,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun CategoryGrid(
    viewModel: BookViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val categoryList = state.categoryList.filter {
        it.type.value == state.transactionType
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        contentPadding = PaddingValues(vertical = 20.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        categoryList.forEachIndexed { index, category ->
            item {
                CategoryItem(
                    icon = category.icon,
                    text = category.name,
                    isSelected = index == state.selectedCategory
                ) {
                    viewModel.updateSelectedCategory(index)
                    viewModel.updateSelectedCategoryId(category.id)
                    Log.d("Category", "Category: ${category.name}")
                    Log.d("Category", "Index: $index")
                    Log.d("Category", "Selected Category: ${state.selectedCategory}")
                    Log.d("Category", "Selected Category Id: ${state.selectedCategoryId}")
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    icon: Int,
    text: String,
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
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
    }
}