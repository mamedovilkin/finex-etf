package io.github.mamedovilkin.network.model.finex

import com.google.gson.annotations.SerializedName

data class ListFund(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("icon_svg") val icon: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("is_active") val isActive: Boolean,
)