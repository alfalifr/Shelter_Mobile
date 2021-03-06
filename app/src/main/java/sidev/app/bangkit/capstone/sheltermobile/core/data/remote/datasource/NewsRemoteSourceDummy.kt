package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object NewsRemoteSourceDummy: NewsRemoteSource {
    override suspend fun getNewsList(startTimestamp: TimeString): Result<List<News>> = Success(Dummy.newsList, 0)

    override suspend fun getArticleList(startTimestamp: TimeString): Result<List<News>> = Success(Dummy.articleList, 0)

    override suspend fun getArticle(timestamp: TimeString): Result<News> = Dummy.articleList.find { it.timestamp.timeLong == timestamp.timeLong }
        ?.let { Success(it, 0) } ?: Util.noValueFailResult()

    override suspend fun getNews(timestamp: TimeString): Result<News> = Dummy.newsList.find { it.timestamp.timeLong == timestamp.timeLong }
    ?.let { Success(it, 0) } ?: Util.noValueFailResult()

    override suspend fun searchNews(keyword: String, startTimestamp: TimeString): Result<List<News>> = Success(
        (Dummy.newsList + Dummy.articleList).filter { it.title.contains(keyword, true) && it.briefDesc.contains(keyword, true) }, 0
    )

    override suspend fun saveNewsList(list: List<News>): Result<Int> = Util.operationNotAvailableFailResult()
}