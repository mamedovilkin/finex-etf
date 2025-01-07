package io.github.mamedovilkin.finexetf.model.network.blog

import com.google.gson.annotations.SerializedName

data class Pagination(
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("total") val total: Int,
)
