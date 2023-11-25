package com.bleh.monify.core.database

import androidx.room.TypeConverter
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
}