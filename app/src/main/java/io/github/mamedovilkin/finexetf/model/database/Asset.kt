package io.github.mamedovilkin.finexetf.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val navPrice: Double,
    val currencyNav: String,
    var quantity: Long,
    val datetime: Long,
    var price: Double,
    val type: String,
)