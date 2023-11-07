package com.bleh.monify.feature_book.book

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.ui.theme.accent
import com.bleh.monify.ui.theme.accentLight
import com.bleh.monify.ui.theme.green
import com.bleh.monify.ui.theme.red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(
    navController: NavController,
    viewModel: BookViewModel
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            AddButton {
                navController.navigate("book_add")
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                viewModel = viewModel
            )
        },
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
            SearchBar(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            BookList(
                viewModel = viewModel,
                paddingBottom = it.calculateBottomPadding()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(40.dp),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon"
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun BookList(
    viewModel: BookViewModel,
    paddingBottom: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = accentLight
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
                        text = "Rp 0",
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
                        text = "Rp 0",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            item {
                BookListPerDay(viewModel = viewModel)
                BookListPerDay(viewModel = viewModel)
                Spacer(modifier = Modifier.height(paddingBottom+85.dp))
            }
        }
    }
}

@Composable
fun BookListPerDay(
    viewModel: BookViewModel,
    modifier: Modifier = Modifier
) {
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
                .background(accentLight)
        ) {
            Text(
                text = "11-01 Rabu",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .weight(1f)
            )
            Text(
                text = "+Rp 3,000,000.00",
                style = MaterialTheme.typography.bodyMedium,
                color = green,
                modifier = Modifier
                    .padding(end = 10.dp)
            )
            Text(
                text = "-Rp 100,000.00",
                style = MaterialTheme.typography.bodyMedium,
                color = red,
                modifier = Modifier
                    .padding(end = 20.dp)
            )
        }
        Column {
            BookListItem(
                viewModel = viewModel,
                icon = R.drawable.ic_food,
                title = "Makanan & Minuman",
                note = "MCD (panas 2 Spicy, Medium)",
                amount = "-Rp 50,000.00",
                amountColor = red,
                wallet = "GoPay"
            )
            BookListItem(
                viewModel = viewModel,
                icon = R.drawable.ic_transportation,
                title = "Transportasi",
                note = "Pulang Kuliah",
                amount = "-Rp 25,000.00",
                amountColor = red,
                wallet = "GoPay"
            )
            BookListItem(
                viewModel = viewModel,
                icon = R.drawable.ic_transportation,
                title = "Transportasi",
                note = "Pergi Kuliah",
                amount = "-Rp 25,000.00",
                amountColor = red,
                wallet = "GoPay"
            )
            BookListItem(
                viewModel = viewModel,
                icon = R.drawable.ic_person_blackboard,
                title = "Gaji",
                note = "Gaji Guru les",
                amount = "-Rp 1,000,000.00",
                amountColor = green,
                wallet = "BCA"
            )
            BookListItem(
                viewModel = viewModel,
                icon = R.drawable.ic_money,
                title = "Uang Bulanan",
                note = "Dari Orang Tua",
                amount = "-Rp 2,000,000.00",
                amountColor = green,
                wallet = "BCA"
            )
        }
    }
}

@Composable
fun BookListItem(
    viewModel: BookViewModel,
    icon: Int,
    title: String,
    note: String,
    amount: String,
    amountColor: Color,
    wallet: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
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
                .padding(start = 10.dp)
                .weight(5f)
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

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = accent,
        contentColor = Color.Black,
        modifier = modifier
            .size(70.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add Icon"
        )
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    viewModel: BookViewModel,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        containerColor = accent,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            BottomBarItem(
                icon = R.drawable.ic_book,
                text = "Buku",
                color = Color.White,
                onClick = {
                    //TODO
                }
            )
            BottomBarItem(
                icon = R.drawable.ic_wallet,
                text = "Dompet",
                onClick = {
                    //TODO
                }
            )
            BottomBarItem(
                icon = R.drawable.ic_analysis,
                text = "Analisis",
                onClick = {
                    //TODO
                }
            )
            BottomBarItem(
                icon = R.drawable.ic_more,
                text = "Lebih",
                onClick = {
                    //TODO
                }
            )
        }
    }
}

@Composable
fun BottomBarItem (
    icon: Int,
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = color
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
    }
}

//@Preview(showSystemUi = true)
@Composable
fun BookScreenPreview() {
    BookScreen(
        navController = NavController(LocalContext.current),
        viewModel = BookViewModel()
    )
}