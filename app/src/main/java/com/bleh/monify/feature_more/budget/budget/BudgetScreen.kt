package com.bleh.monify.feature_more.budget.budget

import android.annotation.SuppressLint
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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.feature_more.budget.BudgetType
import com.bleh.monify.feature_more.budget.BudgetViewModel
import com.bleh.monify.ui.theme.Accent
import kotlinx.coroutines.launch

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
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = state.budgetType.value,
                                    animationSpec = animationSpec
                                )
                            }
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
            item {
                BudgetColumnItem(icon = R.drawable.ic_money, name = "Uang Bulanan", nominal = "Rp 5,000,000.00") {

                }
            }
            item {
                BudgetColumnItem(icon = R.drawable.ic_person_blackboard, name = "Gaji", nominal = "Tidak Diatur") {

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
    modifier: Modifier = Modifier
) {
//TODO
}