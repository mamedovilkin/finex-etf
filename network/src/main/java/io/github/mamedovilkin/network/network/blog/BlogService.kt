package io.github.mamedovilkin.network.network.blog

import io.github.mamedovilkin.network.model.blog.Posts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BlogService {
    @GET("posts/")
    suspend fun getPosts(@Query("key") apiKey: String, @Query("page") page: Int): Response<Posts>
}