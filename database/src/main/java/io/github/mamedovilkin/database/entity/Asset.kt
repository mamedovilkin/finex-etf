package io.github.mamedovilkin.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val isActive: Boolean,
    val navPrice: Double,
    val currencyNav: String,
    var quantity: Long,
    val datetime: Long,
    var price: Double,
    val type: String,
)