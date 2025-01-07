package io.github.mamedovilkin.finexetf.model.network.finex

import com.google.gson.annotations.SerializedName

data class Fund(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("icon_svg") val icon: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("nav") val nav: Nav,
    @SerializedName("text") val text: String,
)
