package com.bleh.monify.feature_analysis.comparison

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bleh.monify.R
import com.bleh.monify.core.data_classes.ChartData
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.ui.theme.Green
import com.bleh.monify.ui.theme.Red
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AnalysisComparisonCard(
    viewModel: AnalysisComparisonViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM").withLocale(Locale("id", "ID"))
    val formatter = indonesianFormatter()
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
                onClick = { viewModel.prevMonth() }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_chevron_left), contentDescription = "left arrow")
            }
            Text(text = state.currentMonth.format(localDateFormatter))
            IconButton(
                onClick = { viewModel.nextMonth() }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_chevron_right), contentDescription = "right arrow")
            }
        }
        val total = state.currentIncome + state.currentOutcome * -1
        val chartDataList = listOf(
            ChartData(
                color = Green,
                data = state.currentIncome.toFloat() * 100f / total.toFloat(),
                icon = painterResource(id = R.drawable.ic_chevron_right)
            ),
            ChartData(
                color = Red,
                data = state.currentOutcome.toFloat() * 100f / total.toFloat() * -1f,
                icon = painterResource(id = R.drawable.ic_chevron_left)
            )
        )
        ComparisonPieChart(
            chartDataList = chartDataList,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
        ComparisonProgressBar(
            progress = (state.currentOutcome.toFloat()*-1f) / (state.currentIncome.toFloat() + (state.currentOutcome.toFloat()*-1f)),
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Text(
                text = "Pengeluaran",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "Pemasukan",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = formatter.format(state.currentOutcome),
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = formatter.format(state.currentIncome),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ComparisonPieChart(
    chartDataList: List<ChartData>,
    modifier: Modifier = Modifier
) {
    val chartList = chartDataList.ifEmpty {
        listOf(
            ChartData(
                color = Color.Gray,
                data = 100f,
                icon = null
            )
        )
    }
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .aspectRatio(1f)
        ) {
            val width = size.width
            val strokeWidth = 40.dp.toPx()

            var startAngle = -90f

            for (index in 0..chartList.lastIndex) {
                val chartData = chartList[index]
                val sweepAngle = chartData.data * 3.6f

                drawArc(
                    color = chartData.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    size = Size(width - strokeWidth, width - strokeWidth),
                    style = Stroke(strokeWidth)
                )

                startAngle += sweepAngle
            }
        }
    }
}

//@Composable
//fun ComparisonBarChart(
//    viewModel: AnalysisComparisonViewModel,
//    modifier: Modifier = Modifier
//) {
//    val state by viewModel.state.collectAsState()
//    val data = listOf(
//        "Pengeluaran" to state.outcome,
//        "Pemasukan" to state.income
//    ).associate { (xValue, yValue) ->
//        xValue to yValue
//    }
//    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
//        data.keys.elementAt(value.toInt())
//    }
//    val outcomeModel = ChartEntryModelProducer(entriesOf(state.outcome, 0f))
//    val incomeModel = ChartEntryModelProducer(entriesOf(0f, state.income))
//    val composedModel = outcomeModel + incomeModel
//    val outcomeChart = columnChart(
//        columns = listOf(
//            LineComponent(
//                color = Red.toArgb(),
//                thicknessDp = 10f,
//                shape = shapeComponent(
//                    shape = RoundedCornerShape(5.dp),
//                ).shape
//            )
//        )
//    )
//    val incomeChart = columnChart(
//        columns = listOf(
//            LineComponent(
//                color = Green.toArgb(),
//                thicknessDp = 10f,
//                shape = shapeComponent(
//                    shape = RoundedCornerShape(5.dp),
//                ).shape
//            )
//        )
//    )
//    Chart(
//        chart = remember(outcomeChart, incomeChart) {outcomeChart + incomeChart},
//        model = composedModel.getModel(),
//        startAxis = rememberStartAxis(
//            title = "Rupiah",
//            titleComponent = textComponent {
//                color = Color.Black.toArgb()
//                typeface = Typeface.SANS_SERIF
//            },
//        ),
//        bottomAxis = rememberBottomAxis(
//            label = textComponent {
//                color = Color.Black.toArgb()
//                typeface = Typeface.SANS_SERIF
//            },
//            guideline = lineComponent(
//                color = Color.Transparent
//            ),
//            valueFormatter = horizontalAxisValueFormatter
//        ),
//        modifier = modifier
//    )
//}

@Composable
fun ComparisonProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Green)
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)

                    // Destination
                    drawContent()

                    // Source
                    drawRect(
                        color = Red,
                        size = Size(size.width * progress, size.height),
                        blendMode = BlendMode.Color,
                    )
                    restoreToCount(checkPoint)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${(progress * 100).toInt()}%",
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}