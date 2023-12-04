package com.bleh.monify.core.data_classes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class ChartData(
    val color: Color,
    val data: Float,
    val icon: Painter?
)