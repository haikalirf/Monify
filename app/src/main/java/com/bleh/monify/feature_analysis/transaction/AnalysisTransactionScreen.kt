package com.bleh.monify.feature_analysis.transaction

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.bleh.monify.R
import com.bleh.monify.core.enums.CategoryType
import com.bleh.monify.core.helper.DistinctColors
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.pojos.CategoryWithSumAndPercentage
import com.bleh.monify.feature_analysis.analysis.AnalysisType
import com.bleh.monify.ui.theme.Green
import com.bleh.monify.ui.theme.Red
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalysisTransactionCard(
    viewModel: AnalysisTransactionViewModel,
    transactionType: AnalysisType,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val transactionList = if (transactionType == AnalysisType.INCOME) state.incomeList else state.outcomeList
    val distinctColors = DistinctColors()
    val chartDataList = transactionList.map {
        ChartData(
            color = distinctColors.next(),
            data = it.percentage.toFloat(),
            icon = painterResource(id = it.category.icon)
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
        ) {
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_chevron_left), contentDescription = "left arrow")
            }
            Text(text = "2023-11")
            IconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_chevron_right), contentDescription = "right arrow")
            }
        }
        AnalysisPieChart(
            chartDataList = chartDataList,
            sum = if(transactionType == AnalysisType.INCOME) state.incomeSum else state.outcomeSum,
            transactionType = transactionType,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(250.dp)
                .padding(bottom = 20.dp)
        )
        AnalysisTransactionList(
            transactionType = if(transactionType == AnalysisType.INCOME) CategoryType.INCOME else CategoryType.OUTCOME,
            categoryList = if(transactionType == AnalysisType.INCOME) state.incomeList else state.outcomeList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

private val Float.degreeToAngle
    get() = (this * Math.PI / 180f).toFloat()

data class ChartData(val color: Color, val data: Float, val icon: Painter)

@Composable
fun AnalysisPieChart(
    transactionType: AnalysisType,
    chartDataList: List<ChartData>,
    sum: Double,
    modifier: Modifier = Modifier
) {
    val formatter = indonesianFormatter()
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val brushColor = if (transactionType == AnalysisType.INCOME) Green else Red
        val brushStyle = Brush.linearGradient(
            colors = listOf(brushColor, brushColor),
        )
        val sign = if (transactionType == AnalysisType.INCOME) "+" else "-"
        val textMeasurer = rememberTextMeasurer()
        val textLayoutResult: TextLayoutResult =
            textMeasurer.measure(
                text = AnnotatedString(text = formatter.format(sum)),
                style = MaterialTheme.typography.labelSmall,
            )
        val textSize = textLayoutResult.size
        Canvas(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .aspectRatio(1f)
        ) {
            val width = size.width
            val radius = width / 2f - 20.dp.toPx()
            val strokeWidth = 40.dp.toPx()

            var startAngle = 0f


            drawText(
                textLayoutResult = textLayoutResult,
                brush = brushStyle,
                topLeft = Offset(
                    width / 2 - textSize.width / 2,
                    width / 2 - textSize.height / 2
                )
            )

            for (index in 0..chartDataList.lastIndex) {
                val chartData = chartDataList[index]
                val sweepAngle = chartData.data * 3.6f
                val angleInRadians = (startAngle + sweepAngle / 2).degreeToAngle

                drawArc(
                    color = chartData.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(width - strokeWidth, width - strokeWidth),
                    style = Stroke(strokeWidth)
                )
                val iconWidth = 24.dp.toPx()

                translate(
                    -iconWidth / 2 + center.x + (radius + strokeWidth) * cos(
                        angleInRadians
                    ),
                    -iconWidth / 2 + center.y + (radius + strokeWidth) * sin(
                        angleInRadians
                    )
                ) {
                    drawCircle(
                        color = chartData.color,
                        radius = 18.dp.toPx(),
                        center = Offset(iconWidth / 2, iconWidth / 2)
                    )
                    with(chartData.icon) {
                        draw(
                            size = Size(iconWidth, iconWidth)
                        )
                    }
                }

                startAngle += sweepAngle
            }
        }
    }
}

@Composable
fun AnalysisTransactionList(
    transactionType: CategoryType,
    categoryList: List<CategoryWithSumAndPercentage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 85.dp),
        modifier = modifier
    ) {
        categoryList.forEach { category ->
            item {
                AnalysisTransactionListItem(
                    transactionType = transactionType,
                    category = category,
                    modifier = Modifier
                ) {

                }
            }
        }
    }
}

@Composable
fun AnalysisTransactionListItem(
    transactionType: CategoryType,
    category: CategoryWithSumAndPercentage,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val formatter = indonesianFormatter()
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
                    onClick()
                }
                .padding(horizontal = 20.dp)
        ) {
            Icon(
                painter = painterResource(id = category.category.icon),
                contentDescription = "Category Icon"
            )
            Text(
                text = category.category.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = String.format("%.2f", category.percentage) + "%",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(end = 10.dp)
            )
            Text(
                text = formatter.format(category.sum),
                style = MaterialTheme.typography.labelSmall,
                color = if (transactionType == CategoryType.INCOME) Green else Red
            )
        }
        Divider(
            thickness = 1.dp,
            color = Color.Black
        )
    }
}