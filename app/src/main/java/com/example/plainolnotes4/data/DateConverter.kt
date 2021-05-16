package com.example.plainolnotes4.data

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun fromDateToTimestamp(date: Date): Long = date.time
}