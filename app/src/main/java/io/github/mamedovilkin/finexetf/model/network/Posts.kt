package io.github.mamedovilkin.finexetf.model.network

import com.google.gson.annotations.SerializedName

data class Posts(
    @SerializedName("posts") val posts: List<Post>
)
