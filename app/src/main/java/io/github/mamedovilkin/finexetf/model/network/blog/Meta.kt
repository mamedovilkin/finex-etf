package io.github.mamedovilkin.finexetf.model.network.blog

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("pagination") val pagination: Pagination,
)
