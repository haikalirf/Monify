package com.bleh.monify.feature_book.add

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.feature_book.TransferWallet
import com.bleh.monify.ui.theme.Accent
import com.bleh.monify.ui.theme.Grey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
fun AddBookScreen(
    navController: NavController,
    viewModel: BookViewModel
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
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
            delay(200)
            Log.d("pager state", "Pager state: ${pagerState.currentPage}")
            viewModel.resetInputState()
            viewModel.updateTransactionType(pagerState.currentPage)
        }
        val animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        LaunchedEffect(state.transactionType) {
            Log.d("transaction type", "Transaction type: ${state.transactionType}")
            pagerState.animateScrollToPage(
                page = state.transactionType,
                animationSpec = animationSpec
            )
        }
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth()
            ) {
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
//                            Log.d("tab position value","Position: $it")
//                            Log.d("tab type value","Type value: $type.value")
                        }
                    }
                }
            }
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            AccentedButton(
                onClick = {
                    navController.navigate("book_main") {
                        popUpTo("book_main") {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            AccentedButton(
                onClick = {
                    navController.navigate("book_main") {
                        popUpTo("book_main") {
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
    val walletList = listOf<TransferWallet>(
        TransferWallet(
            icon = R.drawable.bca,
            name = "BCA",
            nominal = "Rp 1,000,000.00"
        ),
        TransferWallet(
            icon = R.drawable.dana,
            name = "Dana",
            nominal = "Rp 3,000,000.00"
        ),
        TransferWallet(
            icon = R.drawable.gopay,
            name = "Gopay",
            nominal = "Rp 500,000.00"
        ),
        TransferWallet(
            icon = R.drawable.ic_wallet,
            name = "Dompet",
            nominal = "Rp 1,000,000.00"
        ),
        TransferWallet(
            icon = R.drawable.ic_wallet,
            name = "Dompet",
            nominal = "Rp 1,000,000.00"
        ),
        TransferWallet(
            icon = R.drawable.ic_wallet,
            name = "Dompet",
            nominal = "Rp 1,000,000.00"
        ),
        TransferWallet(
            icon = R.drawable.ic_wallet,
            name = "Dompet",
            nominal = "Rp 1,000,000.00"
        ),
    )
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
                walletList = walletList,
                onExpandedChange = {
                    viewModel.updateWalletSourceExpanded(it)
                },
                onWalletSelected = {
                    viewModel.updateWalletSource(it)
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
                walletList = walletList,
                onExpandedChange = {
                    viewModel.updateWalletDestinationExpanded(it)
                },
                onWalletSelected = {
                    viewModel.updateWalletDestination(it)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferWalletDropDown(
    wallet: TransferWallet?,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    walletList: List<TransferWallet> = listOf(),
    onExpandedChange: (Boolean) -> Unit,
    onWalletSelected: (TransferWallet) -> Unit
) {
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
                            text = wallet.nominal,
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
    wallet: TransferWallet,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
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
                text = wallet.nominal,
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
    ExposedDropdownMenuBox(
        expanded = state.isWalletExpanded && state.transactionType == transactionType.value,
        onExpandedChange = {
            viewModel.updateWalletExpanded(!state.isWalletExpanded)
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.wallet,
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
                    painter = painterResource(id = if (state.isWalletExpanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .size(24.dp)
                )
            },
            readOnly = true,
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
//                DropdownMenuItem(text = {}, onClick = {}, modifier = Modifier.height(55.dp))
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.bca,
                    walletName = "BCA",
                    money = "Rp 1,000,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.dana,
                    walletName = "Dana",
                    money = "Rp 3,000,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.gopay,
                    walletName = "Gopay",
                    money = "Rp 500,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.ic_wallet,
                    walletName = "Dompet",
                    money = "Rp 1,000,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.ic_wallet,
                    walletName = "Dompet",
                    money = "Rp 1,000,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.ic_wallet,
                    walletName = "Dompet",
                    money = "Rp 1,000,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.ic_wallet,
                    walletName = "Dompet",
                    money = "Rp 1,000,000.00"
                )
                WalletDropDownItem(
                    viewModel = viewModel,
                    icon = R.drawable.ic_wallet,
                    walletName = "Dompet",
                    money = "Rp 1,000,000.00"
                )
            }
        }
    }
}

@Composable
fun WalletDropDownItem(
    viewModel: BookViewModel,
    icon: Int,
    walletName: String,
    money: String
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
            viewModel.updateWalletState(walletName)
            viewModel.updateWalletExpanded(false)
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
            CategoryItem(
                icon = R.drawable.ic_paper_checkmark,
                text = "Makanan & Minuman",
                isSelected = it == state.selectedCategory
            ) {
                viewModel.updateSelectedCategory(it)
            }
        }
        items(4) {
            Spacer(modifier = Modifier.size(20.dp))
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

//@Preview(showSystemUi = true)
@Composable
fun PreviewAddBookScreen() {
    AddBookScreen(
        navController = NavController(LocalContext.current),
        viewModel = BookViewModel()
    )
}