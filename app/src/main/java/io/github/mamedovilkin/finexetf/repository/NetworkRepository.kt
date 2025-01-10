package io.github.mamedovilkin.finexetf.repository

import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.network.model.blog.Posts
import io.github.mamedovilkin.network.model.cbr.ValCurs
import io.github.mamedovilkin.finexetf.BuildConfig
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val finExService: io.github.mamedovilkin.network.network.finex.FinExService,
    private val cbrService: io.github.mamedovilkin.network.network.cbr.CBRService,
    private val blogService: io.github.mamedovilkin.network.network.blog.BlogService,
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

    suspend fun getPosts(page: Int): Response<Posts> {
        return blogService.getPosts(BuildConfig.BLOG_API_KEY, page)
    }
}