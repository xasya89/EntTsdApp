package com.example.entshptapplication.dao

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }

    @TypeConverter
    fun LocalDateFromTimeStamp(value: Long): LocalDate{
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC).toLocalDate()
    }

    @TypeConverter
    fun localDateToTimestamp(localDate: LocalDate): Long{
        return  localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}