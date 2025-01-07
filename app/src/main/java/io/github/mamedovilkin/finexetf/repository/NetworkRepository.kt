package io.github.mamedovilkin.finexetf.repository

import io.github.mamedovilkin.finexetf.BuildConfig
import io.github.mamedovilkin.finexetf.model.network.finex.Fund
import io.github.mamedovilkin.finexetf.model.network.finex.ListFund
import io.github.mamedovilkin.finexetf.model.network.blog.Posts
import io.github.mamedovilkin.finexetf.model.network.cbr.ValCurs
import io.github.mamedovilkin.finexetf.network.blog.BlogService
import io.github.mamedovilkin.finexetf.network.cbr.CBRService
import io.github.mamedovilkin.finexetf.network.finex.FinExService
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

    suspend fun getPosts(page: Int): Response<Posts> {
        return blogService.getPosts(BuildConfig.BLOG_API_KEY, page)
    }
}