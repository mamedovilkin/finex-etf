package io.github.mamedovilkin.finexetf.model

import com.google.gson.annotations.SerializedName

data class Fond(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("icon_svg") val icon: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
)
