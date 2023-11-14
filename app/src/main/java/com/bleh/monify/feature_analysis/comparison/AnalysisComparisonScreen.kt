package com.bleh.monify.feature_analysis.comparison

import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bleh.monify.R
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.feature_analysis.transaction.AnalysisPieChart
import com.bleh.monify.feature_analysis.transaction.AnalysisTransactionList
import com.bleh.monify.ui.theme.Accent
import com.bleh.monify.ui.theme.Green
import com.bleh.monify.ui.theme.Grey
import com.bleh.monify.ui.theme.Red
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.composed.ComposedChartEntryModel
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AnalysisComparisonCard(
    viewModel: AnalysisComparisonViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
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
        ComparisonBarChart(
            viewModel = viewModel,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 60.dp)
        )
        ComparisonProgressBar(
            progress = state.outcome.toFloat() / state.income.toFloat(),
            modifier = Modifier
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
                text = formatter.format(state.outcome),
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = formatter.format(state.income),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ComparisonBarChart(
    viewModel: AnalysisComparisonViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val data = listOf(
        "Pengeluaran" to state.outcome,
        "Pemasukan" to state.income
    ).associate { (xValue, yValue) ->
        xValue to yValue
    }
    val horizontalAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        data.keys.elementAt(value.toInt())
    }
    val outcomeModel = ChartEntryModelProducer(entriesOf(state.outcome, 0f))
    val incomeModel = ChartEntryModelProducer(entriesOf(0f, state.income))
    val composedModel = outcomeModel + incomeModel
    val outcomeChart = columnChart(
        columns = listOf(
            LineComponent(
                color = Red.toArgb(),
                thicknessDp = 10f,
                shape = shapeComponent(
                    shape = RoundedCornerShape(5.dp),
                ).shape
            )
        )
    )
    val incomeChart = columnChart(
        columns = listOf(
            LineComponent(
                color = Green.toArgb(),
                thicknessDp = 10f,
                shape = shapeComponent(
                    shape = RoundedCornerShape(5.dp),
                ).shape
            )
        )
    )
    Chart(
        chart = remember(outcomeChart, incomeChart) {outcomeChart + incomeChart},
        model = composedModel.getModel(),
        startAxis = rememberStartAxis(
            title = "Rupiah",
            titleComponent = textComponent {
                color = Color.Black.toArgb()
                typeface = Typeface.SANS_SERIF
            },
        ),
        bottomAxis = rememberBottomAxis(
            label = textComponent {
                color = Color.Black.toArgb()
                typeface = Typeface.SANS_SERIF
            },
            guideline = lineComponent(
                color = Color.Transparent
            ),
            valueFormatter = horizontalAxisValueFormatter
        ),
        modifier = modifier
    )
}

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