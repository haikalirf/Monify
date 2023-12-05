package com.bleh.monify.feature_analysis.budget

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.bleh.monify.R
import com.bleh.monify.core.entities.Budget
import com.bleh.monify.core.enums.BudgetType
import com.bleh.monify.core.helper.indonesianFormatter
import com.bleh.monify.core.pojos.BudgetCategory
import com.bleh.monify.ui.theme.Accent
import com.bleh.monify.ui.theme.Grey
import java.text.NumberFormat

@Composable
fun AnalysisBudgetCard(
    viewModel: AnalysisBudgetViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            .background(Color.White)
            .padding(horizontal = 15.dp)
            .padding(top = 20.dp)
            .fillMaxSize()
    ) {
        TimeframeDropDown(
            viewModel = viewModel
        )
        BudgetList(
            viewModel = viewModel,
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeframeDropDown(
    viewModel: AnalysisBudgetViewModel,
) {
    val state by viewModel.state.collectAsState()
    ExposedDropdownMenuBox(
        expanded = state.isDropDownExpanded,
        onExpandedChange = {
            viewModel.updateDropDownExpanded(it)
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = state.budgetTimeFrame.budgetType,
            onValueChange = {},
            shape = RoundedCornerShape(30.dp),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Black,
            ),
            textStyle = MaterialTheme.typography.labelMedium.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
            ),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (state.isDropDownExpanded) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                    contentDescription = "Dropdown",
                    modifier = Modifier
                        .size(24.dp)
                )
            },
            readOnly = true,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(bottom = 5.dp)
                .menuAnchor()
        )
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(
                extraSmall = RoundedCornerShape(24.dp)
            )
        ) {
            DropdownMenu(
                expanded = state.isDropDownExpanded,
                onDismissRequest = {
                    viewModel.updateDropDownExpanded(false)
                },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                ),
                offset = DpOffset(x = 0.dp, y = (-5).dp),
                modifier = Modifier
                    .exposedDropdownSize()
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                if (state.budgetTimeFrame == BudgetType.MONTHLY) {
                    TimeframeDropDownItem(
                        viewModel = viewModel,
                        budgetTimeFrame = BudgetType.WEEKLY,
                    )
                } else {
                    TimeframeDropDownItem(
                        viewModel = viewModel,
                        budgetTimeFrame = BudgetType.MONTHLY,
                    )
                }
            }
        }
    }
}

@Composable
fun TimeframeDropDownItem(
    viewModel: AnalysisBudgetViewModel,
    budgetTimeFrame: BudgetType,
) {
    DropdownMenuItem(
        text = {
            Text(
                text = budgetTimeFrame.budgetType,
                style = MaterialTheme.typography.labelMedium,
            )
        },
        onClick = {
            viewModel.updateBudgetTimeFrame(budgetTimeFrame)
            viewModel.updateDropDownExpanded(false)
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun BudgetList(
    viewModel: AnalysisBudgetViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(bottom = 85.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        state.budgetList.forEach {
            if (state.budgetTimeFrame == BudgetType.MONTHLY && it.budget.monthlyAmount != null) {
                item {
                    BudgetListItem(
                        budgetTimeFrame = state.budgetTimeFrame,
                        budgetCategory = it,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                }
            } else if (state.budgetTimeFrame == BudgetType.WEEKLY && it.budget.weeklyAmount != null) {
                item {
                    BudgetListItem(
                        budgetTimeFrame = state.budgetTimeFrame,
                        budgetCategory = it,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BudgetListItem(
    budgetTimeFrame: BudgetType,
    budgetCategory: BudgetCategory,
    modifier: Modifier = Modifier
) {
    val formatter: NumberFormat = indonesianFormatter()
    val amount = if (budgetTimeFrame == BudgetType.MONTHLY) {
        budgetCategory.budget.monthlyAmount
    } else {
        budgetCategory.budget.weeklyAmount
    } ?: 0.0
    val used = if (budgetTimeFrame == BudgetType.MONTHLY) {
        budgetCategory.budget.monthlyUsed
    } else {
        budgetCategory.budget.weeklyUsed
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 10.dp)
            .padding(bottom = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 5.dp)
        ) {
            Icon(
                painter = painterResource(id = budgetCategory.category.icon),
                contentDescription = "Budget icon"
            )
            Spacer(
                modifier = Modifier
                    .width(20.dp)
            )
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = budgetCategory.category.name,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = formatter.format(amount),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        BudgetProgressBar(
            progress = (used / amount).toFloat(),
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
                text = "Terpakai",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "Sisa",
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
                text = formatter.format(used),
                style = MaterialTheme.typography.bodySmall,
            )
            val isOverBudget = used >= amount
            Text(
                text = formatter.format(amount),
                style = MaterialTheme.typography.bodySmall,
                color = if(isOverBudget) Color.Red else Color.Black
            )
        }
    }
}

@Composable
fun BudgetProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Grey)
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)

                    // Destination
                    drawContent()

                    // Source
                    drawRoundRect(
                        color = Accent,
                        size = Size(size.width * progress, size.height),
                        blendMode = BlendMode.Color,
                        cornerRadius = CornerRadius(30f, 30f)
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