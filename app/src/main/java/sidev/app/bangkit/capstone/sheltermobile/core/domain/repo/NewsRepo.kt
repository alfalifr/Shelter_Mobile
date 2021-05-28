package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.News

interface NewsRepo {
    suspend fun getNewsList(startTimestamp: String): Result<List<News>>
    suspend fun getArticleList(startTimestamp: String): Result<List<News>>
    suspend fun getArticle(timestamp: String): Result<News>
    suspend fun getNews(timestamp: String): Result<News>
    suspend fun searchNews(keyword: String, startTimestamp: String): Result<List<News>>

    suspend fun saveNewsList(list: List<News>): Result<Int>
}