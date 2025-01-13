package io.github.mamedovilkin.network.model.finex

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "funds")
data class ListFund(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @SerializedName("ticker") val ticker: String,
    @SerializedName("icon_svg") val icon: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("is_active") val isActive: Boolean,
)