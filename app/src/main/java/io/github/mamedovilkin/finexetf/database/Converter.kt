package io.github.mamedovilkin.finexetf.database

import androidx.room.TypeConverter
import io.github.mamedovilkin.finexetf.model.database.Type

class Converter {

    companion object {
        @TypeConverter
        fun toType(value: String) = enumValueOf<Type>(value)

        @TypeConverter
        fun fromType(value: Type) = value.name
    }
}