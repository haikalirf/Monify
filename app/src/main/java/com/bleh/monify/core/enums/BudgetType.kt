package com.bleh.monify.core.enums

import com.bleh.monify.R

enum class BudgetType(val value: Int, val budgetType: String, val icon: Int) {
    WEEKLY(0, "Mingguan", R.drawable.ic_weekly),
    MONTHLY(1, "Bulanan", R.drawable.ic_monthly),
}