package com.bleh.monify.feature_book.book

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.pojos.TransactionCategoryWallet
import com.bleh.monify.core.pojos.groupedByDay
import com.bleh.monify.core.ui_components.BottomBar
import com.bleh.monify.core.ui_components.FloatingAddButton
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.feature_more.category.helper.categoryIconList
import com.bleh.monify.ui.theme.AccentLight
import com.bleh.monify.ui.theme.Green
import com.bleh.monify.ui.theme.Red
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BookScreen(
    navController: NavController,
    viewModel: BookViewModel,
) {
    Scaffold(
        floatingActionButton = {
            FloatingAddButton {
                navController.navigate("book_add")
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                bottomBarState = 0
            )
        },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.main_background),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            modifier = Modifier
        ) {
            SearchBar(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            BookList(
                navController = navController,
                viewModel = viewModel,
                paddingBottom = it.calculateBottomPadding()
            )
        }
    }
}

@Composable
fun SearchBar(
    viewModel: BookViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val search = state.searchState
    OutlinedTextField(
        value = search,
        onValueChange = {
            viewModel.updateSearchState(it)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
        ),
        shape = RoundedCornerShape(40.dp),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon"
            )
        },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun BookList(
    navController: NavController,
    viewModel: BookViewModel,
    paddingBottom: Dp,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val formatter = indonesianFormatter()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = AccentLight
            ),
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .padding(10.dp)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(40.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Pemasukan",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatter.format(state.positiveSum),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Pengeluaran",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = formatter.format(state.negativeSum),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
        LazyColumn(
            contentPadding = PaddingValues(bottom = paddingBottom+85.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            state.transactionList.groupedByDay().forEach { (date, transactionList) ->
                item {
                    BookListPerDay(
                        navController = navController,
                        viewModel = viewModel,
                        date = date,
                        transactionList = transactionList,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun BookListPerDay(
    navController: NavController,
    viewModel: BookViewModel,
    date: LocalDate,
    transactionList: List<TransactionCategoryWallet>,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    var showList by remember {
        mutableStateOf(true)
    }
    val formatter = indonesianFormatter()
    val localDateFormatter = DateTimeFormatter.ofPattern("dd-MM EEE").withLocale(Locale("id", "ID"))
    //get both positive sum and negative sum
    val positiveSum = transactionList.filter { transactionCategoryWallet ->
        transactionCategoryWallet.transaction.balance > 0 && transactionCategoryWallet.transaction.walletToId == null
    }.sumOf { transactionCategoryWallet ->
        transactionCategoryWallet.transaction.balance
    }
    val negativeSum = transactionList.filter { transactionCategoryWallet ->
        transactionCategoryWallet.transaction.balance < 0
    }.sumOf { transactionCategoryWallet ->
        transactionCategoryWallet.transaction.balance
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(AccentLight)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                ) {
                    showList = !showList
                }
        ) {
            Text(
                text = localDateFormatter.format(date),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            )
            Text(
                text = formatter.format(positiveSum),
                style = MaterialTheme.typography.bodyMedium,
                color = Green,
                modifier = Modifier
                    .padding(end = 10.dp)
            )
            Text(
                text = formatter.format(negativeSum),
                style = MaterialTheme.typography.bodyMedium,
                color = Red,
                modifier = Modifier
                    .padding(end = 20.dp)
            )
        }
        AnimatedVisibility(
            visible = showList
        ) {
            Column {
                val iconList = categoryIconList()
                transactionList.forEach { transactionCategoryWallet ->
                    var col = if (transactionCategoryWallet.transaction.balance >= 0) Green else Red
                    val icon = transactionCategoryWallet.category?.icon?: R.drawable.ic_transfer
                    var wallet = transactionCategoryWallet.walletFrom.name
                    val isTransfer = transactionCategoryWallet.transaction.isTransfer
                    if (isTransfer) {
                        col = Color.Black
                        wallet = transactionCategoryWallet.walletFrom.name +
                                " > " +
                                transactionCategoryWallet.walletTo!!.name
                    }
                    BookListItem(
                        icon = icon,
                        title = transactionCategoryWallet.category?.name?: "Transfer",
                        note = transactionCategoryWallet.transaction.description,
                        amount = formatter.format(transactionCategoryWallet.transaction.balance),
                        amountColor = col,
                        wallet = wallet,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        viewModel.updateIsEditState(true)
                        viewModel.updateCurrentTransactionId(transactionCategoryWallet.transaction.id)
                        viewModel.updateNoteState(transactionCategoryWallet.transaction.description)
                        viewModel.updateOldNominalState(transactionCategoryWallet.transaction.balance)
                        viewModel.updateNominalState(
                            (transactionCategoryWallet.transaction.balance * if (transactionCategoryWallet.transaction.balance >= 0) 1 else -1).toString()
                        )
                        viewModel.updatePickedDateState(transactionCategoryWallet.transaction.date)
                        viewModel.updateFormattedDate(transactionCategoryWallet.transaction.date)
                        viewModel.updateWalletSource(transactionCategoryWallet.walletFrom)
                        Log.d("BookScreen", "BookListPerDay: ${transactionCategoryWallet.walletFrom.name}")
                        if (isTransfer) {
                            viewModel.updateTransactionType(2)
                            viewModel.updateWalletDestination(transactionCategoryWallet.walletTo!!)
                            viewModel.updateAdminState(transactionCategoryWallet.transaction.admin.toString())
                        } else {
                            viewModel.updateTransactionType(if(transactionCategoryWallet.transaction.balance >= 0) 0 else 1)
                            viewModel.updateSelectedCategory(iconList.indexOf(icon))
                            viewModel.updateSelectedCategoryId(transactionCategoryWallet.category!!.id)
                        }
                        navController.navigate("book_add")
                    }
                }
            }
        }
    }
}

@Composable
fun BookListItem(
    icon: Int,
    title: String,
    note: String,
    amount: String,
    amountColor: Color,
    wallet: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Icon",
            modifier = Modifier
                .weight(1f)
                .scale(1.5f)
        )
        Column(
            modifier = Modifier
                .padding(start = 5.dp)
                .weight(4f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = note,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(3f)
        ) {
            Text(
                text = amount,
                style = MaterialTheme.typography.labelMedium,
                color = amountColor
            )
            Text(
                text = wallet,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.Black
    )
}