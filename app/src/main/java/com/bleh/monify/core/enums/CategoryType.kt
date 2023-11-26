package com.bleh.monify.core.enums

import com.bleh.monify.R

enum class CategoryType(val value: Int, val categoryName: String, val icon: Int) {
    INCOME(0, "Pemasukan", R.drawable.ic_income),
    OUTCOME(1, "Pengeluaran", R.drawable.ic_outcome)
}