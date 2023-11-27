package com.bleh.monify.core.database

import androidx.room.TypeConverter
import com.bleh.monify.core.enums.BudgetType
import com.bleh.monify.core.enums.CategoryType
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun localDateToLong(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }

    @TypeConverter
    fun longToLocalDate(long: Long): LocalDate {
        return LocalDate.ofEpochDay(long)
    }

    @TypeConverter
    fun stringToCategoryType(string: String): CategoryType {
        return CategoryType.valueOf(string)
    }

    @TypeConverter
    fun categoryTypeToString(categoryType: CategoryType): String {
        return categoryType.name
    }

//    @TypeConverter
//    fun stringToBudgetType(string: String): BudgetType {
//        return BudgetType.valueOf(string)
//    }
//
//    @TypeConverter
//    fun budgetTypeToString(budgetType: BudgetType): String {
//        return budgetType.name
//    }
}