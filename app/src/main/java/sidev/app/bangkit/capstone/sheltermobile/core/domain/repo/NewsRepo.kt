package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString

interface NewsRepo {
    suspend fun getNewsList(startTimestamp: TimeString): Result<List<News>>
    suspend fun getArticleList(startTimestamp: TimeString): Result<List<News>>
    suspend fun getArticle(timestamp: TimeString): Result<News>
    suspend fun getNews(timestamp: TimeString): Result<News>
    suspend fun searchNews(keyword: String, startTimestamp: TimeString): Result<List<News>>

    suspend fun saveNewsList(list: List<News>): Result<Int>
}