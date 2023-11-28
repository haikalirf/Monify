package com.bleh.monify.feature_more.budget

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.enums.BudgetType
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.ui.theme.Accent

@Composable
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun BudgetScreen(
    navController: NavController,
    viewModel: BudgetViewModel
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
        ) { 2 }
        LaunchedEffect(pagerState.currentPage) {
            val budgetType = when (pagerState.currentPage) {
                0 -> BudgetType.WEEKLY
                1 -> BudgetType.MONTHLY
                else -> BudgetType.MONTHLY
            }
            viewModel.resetInputState()
            viewModel.updateBudgetType(budgetType)
        }
        val animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        LaunchedEffect(state.budgetType) {
            pagerState.animateScrollToPage(
                page = state.budgetType.value,
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
                    selectedTabPosition = state.budgetType.value,
                    indicatorColor = Accent,
                    modifier = Modifier
                ) {
                    for (type in enumValues<BudgetType>()) {
                        TabTitle(
                            title = type.budgetType,
                            icon = type.icon,
                            position = type.value,
                            textColor = Color.Black,
                            modifier = Modifier
                                .height(50.dp)
                                .width(180.dp)
                        ) {
                            val budgetType = when (type.value) {
                                0 -> BudgetType.WEEKLY
                                1 -> BudgetType.MONTHLY
                                else -> BudgetType.MONTHLY
                            }
                            viewModel.updateBudgetType(budgetType)
                        }
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 1
            ) {
                when (it) {
                    0 -> BudgetCard(viewModel = viewModel, budgetType = BudgetType.WEEKLY, navController = navController)
                    1 -> BudgetCard(viewModel = viewModel, budgetType = BudgetType.MONTHLY, navController = navController)
                }
            }
        }
        val onDismiss = {
            viewModel.updateShowEditDialog(false)
        }
        if(state.showEditDialog) {
            Dialog(
                onDismissRequest = { onDismiss() }
            ) {
                EditBudgetDialog(
                    viewModel = viewModel,
                    budgetType = state.budgetType,
                    modifier = Modifier
                ) {
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    viewModel: BudgetViewModel,
    budgetType: BudgetType,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val formatter = indonesianFormatter()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp, vertical = 20.dp)
    ) {
        LazyColumn(
            modifier = modifier
                .weight(1f)
        ) {
            state.budgetList.forEach { budgetCategory ->
                val amount = if (budgetType == BudgetType.MONTHLY) {
                    budgetCategory.budget.monthlyAmount
                } else {
                    budgetCategory.budget.weeklyAmount
                }
                val nominal = if (amount != null) formatter.format(amount) else "Tidak Diatur"
                item {
                    BudgetColumnItem(
                        icon = budgetCategory.category.icon,
                        name = budgetCategory.category.name,
                        nominal = nominal,
                        onBudgetClicked = {
                            viewModel.updateShowEditDialog(true)
                            viewModel.updateCurrentEditId(id = budgetCategory.budget.id)
                            viewModel.updateCurrentBudgetName(name = budgetCategory.category.name)
                            viewModel.updateCurrentBudgetNominal(nominal = amount)
                        }
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            AccentedButton(
                onClick = {
                    navController.navigate("more") {
                        popUpTo("more_main") {
                            inclusive = true
                        }
                    }
                },
                text = "Kembali",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
        }
    }
}

@Composable
fun BudgetColumnItem(
    icon: Int,
    name: String,
    nominal: String,
    modifier: Modifier = Modifier,
    onBudgetClicked: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable {
                    onBudgetClicked()
                }
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Budget Icon"
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = nominal,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
        }
        Divider(
            thickness = 1.dp,
            color = Color.Black
        )
    }
}

@Composable
fun EditBudgetDialog(
    viewModel: BudgetViewModel,
    budgetType: BudgetType,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var textState by remember { mutableStateOf((state.currentBudgetNominal?: "").toString()) }
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.padding(8.dp),
    ) {
        Column(
            Modifier
                .background(Color.White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = state.currentBudgetName,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(start = 5.dp)
                        .weight(1F)
                )
                IconButton(
                    onClick = {
                        onDismiss()
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
            OutlinedTextField(
                value = textState,
                onValueChange = { textState = it },
                keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal),
                label = { Text("Masukkan Anggaran") },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AccentedButton(
                    onClick = {
                        if(budgetType == BudgetType.MONTHLY) {
                            viewModel.updateMonthlyBudget(state.currentEditId, null)
                        } else {
                            viewModel.updateWeeklyBudget(state.currentEditId, null)
                        }
                        onDismiss()
                    },
                    text = "Hapus",
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                AccentedButton(
                    onClick = {
                        if(budgetType == BudgetType.MONTHLY) {
                            viewModel.updateMonthlyBudget(state.currentEditId, textState.toDouble())
                        } else {
                            viewModel.updateWeeklyBudget(state.currentEditId, textState.toDouble())
                        }
                        onDismiss()
                    },
                    text = "Simpan",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )
            }
        }
    }
}