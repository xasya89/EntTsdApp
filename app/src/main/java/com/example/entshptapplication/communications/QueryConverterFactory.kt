package com.example.entshptapplication.communications

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date


class QueryConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<*, String>? {
        return if (type === Date::class.java) {
            DateQueryConverter.INSTANCE
        } else null
    }

    private class DateQueryConverter : Converter<Date?, String> {
        override fun convert(date: Date?): String {
            return DF.get().format(date)
        }

        companion object {
            val INSTANCE = DateQueryConverter()
            private val DF: ThreadLocal<DateFormat> = object : ThreadLocal<DateFormat>() {
                public override fun initialValue(): DateFormat {
                    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                }
            }
        }
    }

    companion object {
        fun create(): QueryConverterFactory {
            return QueryConverterFactory()
        }
    }
}