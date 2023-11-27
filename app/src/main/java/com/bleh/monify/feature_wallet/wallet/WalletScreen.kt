package com.bleh.monify.feature_wallet.wallet

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.ui_components.BottomBar
import com.bleh.monify.core.ui_components.FloatingAddButton
import com.bleh.monify.feature_wallet.WalletViewModel
import com.bleh.monify.feature_wallet.helper.walletIconList

@Composable
fun WalletScreen(
    navController: NavController,
    viewModel: WalletViewModel,
) {
    Scaffold(
        floatingActionButton = {
            FloatingAddButton {
                navController.navigate("wallet_add")
            }
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                bottomBarState = 1
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
            WalletTotalComposable(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            WalletList(
                navController = navController,
                viewModel = viewModel,
                columnContentPadding = it,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun WalletTotalComposable(
    viewModel: WalletViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val formatter = indonesianFormatter()
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Total Dompet",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = formatter.format(state.totalNominal),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
fun WalletList(
    navController: NavController,
    viewModel: WalletViewModel,
    modifier: Modifier = Modifier,
    columnContentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val state by viewModel.state.collectAsState()
    val formatter = indonesianFormatter()
    val iconList = walletIconList()
    LazyColumn(
        contentPadding = PaddingValues(top = 20.dp, bottom = columnContentPadding.calculateBottomPadding()+85.dp),
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
    ) {
        item {
            state.wallets.forEachIndexed { index, wallet ->
                if(!wallet.isDeleted) {
                    WalletListItem(
                        icon = wallet.icon,
                        name = wallet.name,
                        nominal = formatter.format(wallet.balance),
                        onClick = {
                            viewModel.updateIsEditState(true)
                            viewModel.updateWalletNameState(wallet.name)
                            viewModel.updateWalletNominalState(wallet.balance.toString())
                            viewModel.updateSelectedWalletState(iconList.indexOf(wallet.icon))
                            viewModel.updateCurrentEditIdState(wallet.id)
                            navController.navigate("wallet_add")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WalletListItem(
    icon: Int,
    name: String,
    nominal: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .padding(vertical = 5.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = name,
                modifier = Modifier
                    .size(36.dp)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
            Text(
                text = nominal,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
            )
        }
        Divider(
            thickness = 1.dp,
            color = Color.Black,
            modifier = Modifier
        )
    }
}