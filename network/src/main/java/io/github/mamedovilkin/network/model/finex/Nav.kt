package io.github.mamedovilkin.network.model.finex

import com.google.gson.annotations.SerializedName

data class Nav(
    @SerializedName("nav_per_share") val navPerShare: Double,
    @SerializedName("currency_nav") val currencyNav: String,
)