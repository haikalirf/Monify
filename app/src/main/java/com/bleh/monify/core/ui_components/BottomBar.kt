package com.bleh.monify.core.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.feature_book.BookViewModel
import com.bleh.monify.ui.theme.Accent

@Composable
fun BottomBar(
    navController: NavController,
    bottomBarState: Int,
) {
    BottomAppBar(
        containerColor = Accent,
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
                color = if (bottomBarState == 0) Color.White else Color.Black,
                onClick = {
                    if (bottomBarState != 0) {
                        navController.navigate("book")
                    }
                }
            )
            BottomBarItem(
                icon = R.drawable.ic_wallet,
                text = "Dompet",
                color = if (bottomBarState == 1) Color.White else Color.Black,
                onClick = {
                    if (bottomBarState != 1) {
                        navController.navigate("wallet")
                    }
                }
            )
            BottomBarItem(
                icon = R.drawable.ic_analysis,
                text = "Analisis",
                color = if (bottomBarState == 2) Color.White else Color.Black,
                onClick = {
                    if (bottomBarState != 2) {
                        navController.navigate("analysis")
                    }
                }
            )
            BottomBarItem(
                icon = R.drawable.ic_more,
                text = "Lebih",
                color = if (bottomBarState == 3) Color.White else Color.Black,
                onClick = {
                    if (bottomBarState != 3) {
                        navController.navigate("more")
                    }
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