package ru.kropotov.denet.test.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun stringListToString(list: List<String>): String =
        list.joinToString(separator = ";")

    @TypeConverter fun stringToStringList(value: String): List<String> =
        value.split(";").filter { it.isNotEmpty() }

}