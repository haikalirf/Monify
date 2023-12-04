package com.bleh.monify.feature_more.category

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.navigation.compose.rememberNavController
import com.bleh.monify.R
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.core.ui_components.AccentedButton
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.feature_more.category.helper.categoryIconList
import com.bleh.monify.feature_wallet.helper.walletIconList
import com.bleh.monify.ui.theme.Accent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun CategoryScreen(
    navController: NavController,
    viewModel: CategoryViewModel
) {
    val state by viewModel.state.collectAsState()
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
            val categoryType = when (pagerState.currentPage) {
                0 -> CategoryType.INCOME
                1 -> CategoryType.OUTCOME
                else -> CategoryType.INCOME
            }
            viewModel.resetInputState()
            viewModel.updateCategoryType(categoryType)
            Log.d("CategoryScreen", "Category Type: ${categoryType.categoryName}")
        }
        val animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        LaunchedEffect(state.categoryType) {
            pagerState.animateScrollToPage(
                page = state.categoryType.value,
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
                    selectedTabPosition = state.categoryType.value,
                    indicatorColor = Accent,
                    modifier = Modifier
                ) {
                    for (type in enumValues<CategoryType>()) {
                        TabTitle(
                            title = type.categoryName,
                            icon = type.icon,
                            position = type.value,
                            textColor = Color.Black,
                            modifier = Modifier
                                .height(50.dp)
                                .width(180.dp)
                        ) {
                            val categoryType = when (type.value) {
                                0 -> CategoryType.INCOME
                                1 -> CategoryType.OUTCOME
                                else -> CategoryType.INCOME
                            }
                            viewModel.updateCategoryType(categoryType)
                        }
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 1
            ) {
                when (it) {
                    0 -> CategoryCard(viewModel = viewModel, categoryType = CategoryType.INCOME, navController = navController)
                    1 -> CategoryCard(viewModel = viewModel, categoryType = CategoryType.OUTCOME, navController = navController)
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    viewModel: CategoryViewModel,
    categoryType: CategoryType,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val iconList = categoryIconList()
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
            state.categories.forEach { category ->
                if (category.type == categoryType && !category.isDeleted) {
                    item {
                        CategoryColumnItem(
                            icon = category.icon,
                            name = category.name,
                            onCategoryClicked = {
                                viewModel.updateIsEditState(true)
                                viewModel.updateCategoryName(category.name)
                                viewModel.updateSelectedCategory(iconList.indexOf(category.icon))
                                viewModel.updateCurrentEditIdState(category.id)
                                navController.navigate("category_add")
                            }
                        )
                    }
                }
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
            AccentedButton(
                onClick = {
                    viewModel.updateCategoryType(categoryType)
                    navController.navigate("category_add")
                },
                text = "Tambah",
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
        }
    }
}

@Composable
fun CategoryColumnItem(
    icon: Int,
    name: String,
    modifier: Modifier = Modifier,
    onCategoryClicked: () -> Unit
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
                    onCategoryClicked()
                }
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Category Icon"
            )
            Text(
                text = name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_triple_bar),
                contentDescription = "Arrow Right",
            )
        }
        Divider(
            thickness = 1.dp,
            color = Color.Black
        )
    }
}