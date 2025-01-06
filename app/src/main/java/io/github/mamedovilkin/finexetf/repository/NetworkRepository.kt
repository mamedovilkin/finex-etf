package io.github.mamedovilkin.finexetf.repository

import io.github.mamedovilkin.finexetf.BuildConfig
import io.github.mamedovilkin.finexetf.model.network.Fund
import io.github.mamedovilkin.finexetf.model.network.ListFund
import io.github.mamedovilkin.finexetf.model.network.Posts
import io.github.mamedovilkin.finexetf.model.network.ValCurs
import io.github.mamedovilkin.finexetf.network.BlogService
import io.github.mamedovilkin.finexetf.network.CBRService
import io.github.mamedovilkin.finexetf.network.FinExService
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val finExService: FinExService,
    private val cbrService: CBRService,
    private val blogService: BlogService,
) {

    suspend fun getFunds(): Response<List<ListFund>> {
        return finExService.getFunds()
    }

    suspend fun getFund(ticker: String): Response<Fund> {
        return finExService.getFund(ticker)
    }

    suspend fun getCurrencies(dateReq: String): Response<ValCurs> {
        return cbrService.getCurrencies(dateReq)
    }

    suspend fun getPosts(): Response<Posts> {
        return blogService.getPosts(BuildConfig.BLOG_API_KEY)
    }

    suspend fun getPost(id: String): Response<Posts> {
        return blogService.getPost(id, BuildConfig.BLOG_API_KEY)
    }
}