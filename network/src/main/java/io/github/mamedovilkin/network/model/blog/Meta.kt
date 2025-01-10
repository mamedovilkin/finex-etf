package io.github.mamedovilkin.network.model.blog

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("pagination") val pagination: Pagination,
)
