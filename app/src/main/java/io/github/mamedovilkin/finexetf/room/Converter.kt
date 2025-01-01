package io.github.mamedovilkin.finexetf.room

import androidx.room.TypeConverter

class Converter {

    companion object {
        @TypeConverter
        fun toType(value: String) = enumValueOf<Type>(value)

        @TypeConverter
        fun fromType(value: Type) = value.name
    }
}