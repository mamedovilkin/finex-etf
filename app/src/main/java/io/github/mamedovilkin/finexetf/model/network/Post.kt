package io.github.mamedovilkin.finexetf.model.network

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("html") val html: String,
    @SerializedName("feature_image") val featureImage: String,
    @SerializedName("published_at") val publishedAt: String,
    @SerializedName("url") val url: String,
    @SerializedName("excerpt") val excerpt: String,
)
