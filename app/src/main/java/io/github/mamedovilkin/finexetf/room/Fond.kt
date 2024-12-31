package io.github.mamedovilkin.finexetf.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Fond(
    @PrimaryKey(autoGenerate = false)
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val quantity: Int,
    val datetimePurchase: Long,
    val type: String = Converter.fromType(Type.PURCHASE),
)