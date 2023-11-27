package com.bleh.monify.feature_analysis.analysis

import android.annotation.SuppressLint
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bleh.monify.R
import com.bleh.monify.core.ui_components.BottomBar
import com.bleh.monify.core.ui_components.SelectionBar
import com.bleh.monify.core.ui_components.TabTitle
import com.bleh.monify.feature_analysis.budget.AnalysisBudgetCard
import com.bleh.monify.feature_analysis.budget.AnalysisBudgetViewModel
import com.bleh.monify.feature_analysis.comparison.AnalysisComparisonCard
import com.bleh.monify.feature_analysis.comparison.AnalysisComparisonViewModel
import com.bleh.monify.feature_analysis.transaction.AnalysisTransactionViewModel
import com.bleh.monify.feature_analysis.transaction.TransactionAnalysisCard
import com.bleh.monify.ui.theme.Accent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: AnalysisViewModel,
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                bottomBarState = 2
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier
            .paint(
                painter = painterResource(R.drawable.main_background),
                contentScale = ContentScale.Crop
            )
    ) {
        val pagerState = rememberPagerState(
            initialPage = 1,
        ) { 4 }
        LaunchedEffect(pagerState.currentPage) {
            delay(200)
            val analysisType = when (pagerState.currentPage) {
                0 -> AnalysisType.INCOME
                1 -> AnalysisType.OUTCOME
                2 -> AnalysisType.BUDGET
                3 -> AnalysisType.COMPARISON
                else -> AnalysisType.OUTCOME
            }
            viewModel.updateAnalysisType(analysisType)
        }
        val animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = FastOutSlowInEasing)
        LaunchedEffect(state.analysisType) {
            pagerState.animateScrollToPage(
                page = state.analysisType.value,
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
                    selectedTabPosition = state.analysisType.value,
                    indicatorColor = Accent,
                    modifier = Modifier
                ) {
                    for (type in enumValues<AnalysisType>()) {
                        TabTitle(
                            icon = type.icon,
                            position = type.value,
                            textColor = Color.Black,
                            modifier = Modifier
                                .height(50.dp)
                                .width(95.dp)
                        ) {
                            val analysisType = when (type.value) {
                                0 -> AnalysisType.INCOME
                                1 -> AnalysisType.OUTCOME
                                2 -> AnalysisType.BUDGET
                                3 -> AnalysisType.COMPARISON
                                else -> AnalysisType.OUTCOME
                            }
                            viewModel.updateAnalysisType(analysisType)
                        }
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 3,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (it) {
                    0 -> TransactionAnalysisCard(
                        viewModel = AnalysisTransactionViewModel(),
                        transactionType = AnalysisType.INCOME,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    1 -> TransactionAnalysisCard(
                        viewModel = AnalysisTransactionViewModel(),
                        transactionType = AnalysisType.OUTCOME,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    2 -> AnalysisBudgetCard(
                        viewModel = AnalysisBudgetViewModel(),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    3 -> AnalysisComparisonCard(
                        viewModel = AnalysisComparisonViewModel(),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}