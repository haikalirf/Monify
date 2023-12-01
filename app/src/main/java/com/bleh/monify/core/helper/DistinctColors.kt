package com.bleh.monify.core.helper

import androidx.compose.ui.graphics.Color

class DistinctColors() {
    private val colors = listOf(
        Color(0xFFFFB300),    // Vivid Yellow
        Color(0xFF803E75),    // Strong Purple
        Color(0xFFFF6800),    // Vivid Orange
        Color(0xFFA6BDD7),    // Very Light Blue
        Color(0xFFC10020),    // Vivid Red
        Color(0xFFCEA262),    // Grayish Yellow
        Color(0xFF817066),    // Medium Gray
        Color(0xFF007D34),    // Vivid Green
        Color(0xFFF6768E),    // Strong Purplish Pink
        Color(0xFF00538A),    // Strong Blue
        Color(0xFFFF7A5C),    // Strong Yellowish Pink
        Color(0xFF53377A),    // Strong Violet
        Color(0xFFFF8E00),    // Vivid Orange Yellow
        Color(0xFFB32851),    // Strong Purplish Red
        Color(0xFFF4C800),    // Vivid Greenish Yellow
        Color(0xFF7F180D),    // Strong Reddish Brown
        Color(0xFF93AA00),    // Vivid Yellowish Green
        Color(0xFF593315),    // Deep Yellowish Brown
        Color(0xFFF13A13),    // Vivid Reddish Orange
        Color(0xFF232C16)     // Dark Olive Green
    )
    private var index = 0

    fun next(): Color {
        val color = colors[index]
        index = (index + 1) % colors.size
        return color
    }
}