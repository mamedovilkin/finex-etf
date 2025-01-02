package io.github.mamedovilkin.finexetf.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Fund(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val ticker: String,
    val icon: String,
    val name: String,
    val originalName: String,
    val navPrice: Double,
    val quantity: Int,
    val datetime: Long,
    val price: Double,
    val type: String,
)