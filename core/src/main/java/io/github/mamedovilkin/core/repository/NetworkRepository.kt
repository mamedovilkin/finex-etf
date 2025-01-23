package io.github.mamedovilkin.core.repository

import io.github.mamedovilkin.network.model.finex.Fund
import io.github.mamedovilkin.network.model.finex.ListFund
import io.github.mamedovilkin.network.model.blog.Posts
import io.github.mamedovilkin.network.model.cbr.ValCurs
import io.github.mamedovilkin.network.BuildConfig
import io.github.mamedovilkin.network.api.blog.BlogService
import io.github.mamedovilkin.network.api.cbr.CBRService
import io.github.mamedovilkin.network.api.finex.FinExService
import io.github.mamedovilkin.network.dao.FundDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val finExService: FinExService,
    private val cbrService: CBRService,
    private val blogService: BlogService,
    private val fundDao: FundDao,
) {

    val cachedFunds: Flow<List<ListFund>> = flow {
        emit(fundDao.getAllFunds())
        val fundsResponse = finExService.getFunds()
        if (fundsResponse.isSuccessful && fundsResponse.body() != null) {
            val funds = fundsResponse.body() as List<ListFund>
            fundDao.deleteAllFunds()
            fundDao.insertFunds(funds)
            emit(funds)
        } else {
            emit(fundDao.getAllFunds())
        }
    }.flowOn(Dispatchers.IO)

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