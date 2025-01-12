package io.github.mamedovilkin.network.model.finex

import com.google.gson.annotations.SerializedName

data class Fund(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("icon_svg") val icon: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("nav") val nav: Nav,
    @SerializedName("text") val text: String,
)
