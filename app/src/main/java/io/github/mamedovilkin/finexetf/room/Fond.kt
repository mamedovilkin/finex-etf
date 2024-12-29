package io.github.mamedovilkin.finexetf.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Fond(
    @PrimaryKey(autoGenerate = false)
    val ticker: String,
    val quantity: Int,
    val datetimePurchase: Long,
)