package io.github.mamedovilkin.finexetf.network

import io.github.mamedovilkin.finexetf.model.network.Posts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BlogService {
    @GET("posts/")
    suspend fun getPosts(@Query("key") apiKey: String): Response<Posts>

    @GET("posts/{id}/")
    suspend fun getPost(@Path("id") id: String, @Query("key") apiKey: String): Response<Posts>
}