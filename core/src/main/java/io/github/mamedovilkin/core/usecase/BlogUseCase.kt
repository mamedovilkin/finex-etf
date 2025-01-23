package io.github.mamedovilkin.core.usecase

import io.github.mamedovilkin.core.repository.NetworkRepository
import io.github.mamedovilkin.network.model.blog.Posts
import retrofit2.Response
import javax.inject.Inject

class BlogUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {

    suspend fun getPosts(page: Int): Response<Posts> {
        return networkRepository.getPosts(page)
    }
}