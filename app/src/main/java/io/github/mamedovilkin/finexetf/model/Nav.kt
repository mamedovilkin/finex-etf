package io.github.mamedovilkin.finexetf.model

import com.google.gson.annotations.SerializedName

data class Nav(
    @SerializedName("nav_per_share") val navPerShare: Double,
)