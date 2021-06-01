package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.NewsApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.NewsBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import java.lang.IllegalStateException

class NewsRemoteSourceImpl(private val api: NewsApi): NewsRemoteSource {
    override suspend fun getNewsList(startTimestamp: String): Result<List<News>> = getDataList(startTimestamp, Const.TYPE_NEWS)

    override suspend fun getArticleList(startTimestamp: String): Result<List<News>> = getDataList(startTimestamp, Const.TYPE_ARTICLE)

    private suspend fun getDataList(startTimestamp: String, type: Int): Result<List<News>> {
        val body = NewsBody(type)
        val call = api.getNews(body)
        return call.toListResult {
            it.toModel(type)
        }
    }

    override suspend fun getArticle(timestamp: String): Result<News> = getData(timestamp, Const.TYPE_ARTICLE)

    override suspend fun getNews(timestamp: String): Result<News> = getData(timestamp, Const.TYPE_NEWS)

    private suspend fun getData(timestamp: String, type: Int): Result<News> = when(val res = getDataList(timestamp, type)) {
        is Success -> {
            //val time = Util.getTimestampStr(timestamp)
            val news = res.data.find { it.timestamp == timestamp }
            if(news != null) Success(news, 0)
            else Util.noEntityFailResult()
        }
        is Fail -> res
    }

    override suspend fun searchNews(keyword: String, startTimestamp: String): Result<List<News>> {
        val newsRes = searchNewsEachType(keyword, startTimestamp, Const.TYPE_NEWS)
        val articleRes = searchNewsEachType(keyword, startTimestamp, Const.TYPE_ARTICLE)

        if(newsRes is Fail && articleRes is Fail)
            return newsRes

        var code = 0
        val allNewsList = mutableListOf<News>()

        if(newsRes is Success) {
            allNewsList += newsRes.data
        } else {
            code = 1
        }
        if(articleRes is Success) {
            allNewsList += articleRes.data
        } else {
            code = 1
        }

        return Success(allNewsList, code)
    }

    private suspend fun searchNewsEachType(keyword: String, startTimestamp: String, type: Int): Result<List<News>> = when(val res = getDataList(startTimestamp, type)) {
        is Success -> {
            val newsList = res.data.filter { it.title.contains(keyword, true) || it.briefDesc.contains(keyword, true) }
            Success(newsList, 0)
        }
        is Fail -> res
    }

    override suspend fun saveNewsList(list: List<News>): Result<Int> {
        val msg = "Can't save `News` to remote source"
        return Fail(msg, -1, IllegalStateException(msg))
    }
}