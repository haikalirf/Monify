package com.bleh.monify.feature_book.editor

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.ui.theme.Accent
import com.bleh.monify.ui.theme.Grey
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
            viewModel.resetInputState()
            viewModel.updateTransactionType(pagerState.currentPage)
        }
        val animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = FastOutSlowInEasing)
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
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = state.transactionType,
                                    animationSpec = animationSpec
                                )
                            }
                        }
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 2
            ) {
                when (it) {
                    0 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.INCOME)
                    1 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.OUTCOME)
                    2 -> TransactionCard(viewModel = viewModel, transactionType = TransactionType.TRANSFER)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCard(
    viewModel: BookViewModel,
    transactionType: TransactionType,
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
            text = "Catatan " + if (state.transactionType == 0) "Pemasukan" else "Pengeluaran",
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .weight(1f)
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
                    viewModel.updateSelectedItem(it)
                }
            }
            items(4) {
                Spacer(modifier = Modifier.size(20.dp))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            AccentedButton(
                onClick = { /*TODO*/ },
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
                .zIndex(2f)
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
                offset = DpOffset(x = 0.dp, y = (-55).dp),
                modifier = Modifier
                    .exposedDropdownSize()
                    .background(Color.White)
                    .height(280.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .zIndex(1f)
            ) {
                DropdownMenuItem(text = {}, onClick = {}, modifier = Modifier.height(55.dp))
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
            .zIndex(1f)
    )
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