package io.github.mamedovilkin.finexetf.model.network.blog

import com.google.gson.annotations.SerializedName

data class Posts(
    @SerializedName("posts") val posts: List<Post>,
    @SerializedName("meta") val meta: Meta,
)
