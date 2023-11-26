package com.bleh.monify.core.helper

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class RupiahVisualTransformation: VisualTransformation  {
    override fun filter(text: AnnotatedString): TransformedText {
        var output = ""
        for (i in text.text.indices) {
            if (i == 0) output += "$ "
            output += text.text[i]
        }
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                return text.text.length + 2
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                return offset -2
            }
        }
        return TransformedText(AnnotatedString(output), numberOffsetTranslator)
    }
}